package com.portfolio.expense_tracker.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final String code;
    private final int httpStatusCode;
    private final String reason;
    private final String message;

    public BusinessException(ExceptionCode exceptionCode, String message) {
        super(message);
        this.code = exceptionCode.getCode();
        this.httpStatusCode = exceptionCode.getHttpStatusCode();
        this.reason = exceptionCode.getReason();
        this.message = message;
    }
}
