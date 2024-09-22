package com.portfolio.expensetracker.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.expensetracker.exception.AuthenticationFailedException;
import com.portfolio.expensetracker.exception.BusinessException;
import com.portfolio.expensetracker.exception.ExceptionDto;
import com.portfolio.expensetracker.mapper.ExceptionMapperEntryPointRest;
import com.portfolio.expensetracker.util.AuthenticationConstants;
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
import org.jose4j.keys.HmacKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

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
    @Value("${auth.enabled:true}")
    private boolean authEnabled;

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
    public void init() {
        if (authEnabled) {
            if (!StringUtils.hasText(secret) ||
                    secret.length() != AuthenticationConstants.Authentication.Jwt.ALGORITHM_KEY_SIZE_BYTES) {

                throw new AuthenticationFailedException("Jwt signature (HMAC) key is not properly set.");
            }

            key = new HmacKey(secret.getBytes());
        }
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (authEnabled) {
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

            } catch (BusinessException e) {
                returnError(e, response);
            } catch (Exception e) {
                throw new AuthenticationFailedException(e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
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
