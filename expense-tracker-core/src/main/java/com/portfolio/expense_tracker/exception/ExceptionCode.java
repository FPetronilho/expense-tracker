package com.portfolio.expense_tracker.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@RequiredArgsConstructor
public enum ExceptionCode {

    INTERNAL_SERVER_ERROR("E-001", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error."),
    RESOURCE_NOT_FOUND("E-002", HttpStatus.NOT_FOUND.value(), "Resource not found."),
    RESOURCE_ALREADY_EXISTS("E-003", HttpStatus.CONFLICT.value(), "Resource already exists."),
    CLIENT_NOT_AUTHENTICATED("E-004", HttpStatus.UNAUTHORIZED.value(), "Client not authenticated."),
    CLIENT_NOT_AUTHORIZED("E-005", HttpStatus.FORBIDDEN.value(), "Client not authorized."),
    CONFIGURATION_ERROR("E-006", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Configuration error."),
    PARAMETER_VALIDATION_ERROR("E-007", HttpStatus.BAD_REQUEST.value(), "Parameter validation error.");

    private final String code;
    private final int httpStatusCode;
    private final String reason;
}
