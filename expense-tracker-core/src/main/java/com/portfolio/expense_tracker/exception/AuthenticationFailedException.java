package com.portfolio.expense_tracker.exception;

public class AuthenticationFailedException extends BusinessException {

    public AuthenticationFailedException(String message) {
        super(
                ExceptionCode.CLIENT_NOT_AUTHENTICATED,
                message
        );
    }
}
