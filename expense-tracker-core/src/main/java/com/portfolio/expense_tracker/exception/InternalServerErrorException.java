package com.portfolio.expense_tracker.exception;

public class InternalServerErrorException extends BusinessException {

    private static final String ERROR_MESSAGE = "Internal server error.";

    public InternalServerErrorException() {
        super(
                ExceptionCode.INTERNAL_SERVER_ERROR,
                ERROR_MESSAGE
        );
    }
}
