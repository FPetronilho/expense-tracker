package com.portfolio.expense_tracker.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.expense_tracker.exception.AuthenticationFailedException;
import com.portfolio.expense_tracker.exception.BusinessException;
import com.portfolio.expense_tracker.exception.ExceptionCode;
import com.portfolio.expense_tracker.exception.ExceptionDto;
import com.portfolio.expense_tracker.mapper.ExceptionMapperEntryPointRest;
import com.portfolio.expense_tracker.util.AuthenticationConstants;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.awt.*;
import java.io.IOException;
import java.security.Key;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {

    private final ExceptionMapperEntryPointRest mapper;
    private final ObjectMapper jsonMapper;
    private Key key;

    @Value("${auth.jwt.secret}")
    private String secret;

    private static void setSecurityContext(
            HttpServletRequest request,
            JwtClaims claims
    ) throws MalformedClaimException {

        String subject = claims.getSubject();
        String scopes = claims.getClaimValueAsString(AuthenticationConstants.Authentication.Jwt.Claim.SCOPE.getValue());
        List<SimpleGrantedAuthority> authorities;

        if (StringUtils.hasText(scopes)) {
            authorities = Arrays.stream(scopes.split(" "))
                    .map(scope -> new SimpleGrantedAuthority(scope))
                    .collect(Collectors.toList());
        } else {
            authorities = Collections.emptyList();
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                subject,
                null,
                authorities
        );

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @PostConstruct
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = request.getHeader(AuthenticationConstants.Authentication.HTTP_HEADER_AUTHORIZATION);
            if (!StringUtils.hasText(jwt)) {
                throw new AuthenticationFailedException(
                        String.format(
                                "invalid JWT given in HTTP header %s",
                                AuthenticationConstants.Authentication.HTTP_HEADER_AUTHORIZATION
                        )
                );
            }

            jwt = jwt.substring(AuthenticationConstants.Authentication.Jwt.PREFIX.length());

            JwtClaims claims = validateJwt(jwt);

            setSecurityContext(request, claims);

            filterChain.doFilter(request, response);

        } catch (BusinessException e) {
            returnError(e, response);
        } catch (Exception e) {
            BusinessException businessException = new BusinessException(
                    ExceptionCode.CLIENT_NOT_AUTHENTICATED,
                    e.getMessage()
            );
        }
    }

    private void returnError(
            BusinessException e,
            HttpServletResponse response
    ) throws IOException {
        ExceptionDto exceptionDto = mapper.toExceptionDto(e);
        String json = jsonMapper.writeValueAsString(exceptionDto);
        response.setStatus(exceptionDto.getHttpStatusCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setContentLength(json.length());
        response.getWriter().print(json);
        response.getWriter().flush();
    }

    private JwtClaims validateJwt(String jwt) {

        JwtConsumer consumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(AuthenticationConstants.Authentication.Jwt.ALLOWED_CLOCK_SKEW_SECONDS)
                .setRequireSubject()
                .setRequireIssuedAt()
                .setExpectedIssuer(AuthenticationConstants.Authentication.Jwt.ISSUER)
                .setExpectedAudience(AuthenticationConstants.Authentication.Jwt.AUDIENCE)
                .setJwsAlgorithmConstraints(
                        AlgorithmConstraints.ConstraintType.PERMIT,
                        AlgorithmIdentifiers.HMAC_SHA512
                )
                .setVerificationKey(key)
                .build();

        try {
            return consumer.processToClaims(jwt);
        } catch (Exception e) {
            throw new AuthenticationFailedException(e.getMessage());
        }
    }
}
