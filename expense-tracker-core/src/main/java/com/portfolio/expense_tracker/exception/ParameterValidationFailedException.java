package com.portfolio.expense_tracker.exception;

public class ParameterValidationFailedException extends BusinessException {

    public ParameterValidationFailedException(String message) {
        super(
                ExceptionCode.PARAMETER_VALIDATION_ERROR,
                message
        );
    }
}
