package com.portfolio.expensetracker.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.expensetracker.exception.*;
import com.portfolio.expensetracker.mapper.ExceptionMapperEntryPointRest;
import com.portfolio.expensetracker.util.AuthenticationConstants;
import com.portfolio.expensetracker.util.Environment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final ExceptionMapperEntryPointRest mapper;
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final AuthenticationFilter filter;

    @Value("${spring.application.environment}")
    private String environment;

    @Bean
    @ConditionalOnProperty(
            name = "auth.enabled",
            havingValue = "false"
    )
    public SecurityFilterChain noSecurityFilterChain(HttpSecurity security) throws Exception {
        log.warn("Authentication is disabled!");
        if (!Environment.LOCAL.getValue().equalsIgnoreCase(environment)) {
            throw new ConfigurationErrorException(
                    String.format(
                            "Authentication must be enabled for any non-local environment: %s",
                            Stream.of(Environment.values())
                                    .filter(env -> !env.equals(Environment.LOCAL))
                                    .map(Environment::getValue)
                                    .collect(Collectors.toSet())
                    )
            );
        }

        return security.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .build();
    }

    @Bean
    @ConditionalOnProperty(
            name = "auth.enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        configureAuthorization(security);
        security.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return security.build();
    }

    private void configureAuthorization(HttpSecurity security) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry =
                security.authorizeRequests();

        for (AuthenticationConstants.Authentication.Scope scope :
                AuthenticationConstants.Authentication.Scope.values()) {

            registry.requestMatchers(
                    scope.getHttpMethod(),
                    scope.getUri(),
                    scope.getValue()
            );
        }

        registry.anyRequest().authenticated();
        registry.and().exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {

             ExceptionDto exceptionDto = mapper.toExceptionDto(
                     new AuthorizationFailedException(
                             String.format("Not authorized to access: %s", request.getRequestURI())
            ));

            String json = jsonMapper.writeValueAsString(exceptionDto);
            response.setStatus(exceptionDto.getHttpStatusCode());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setContentLength(json.length());
            response.getWriter().print(json);
            response.getWriter().flush();
        };
    }
}
