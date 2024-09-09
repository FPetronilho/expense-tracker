package com.portfolio.expense_tracker.exception;

public class ConfigurationErrorException extends BusinessException {

    private static final String ERROR_MESSAGE = "Configuration error.";

    public ConfigurationErrorException() {
        super(
                ExceptionCode.CONFIGURATION_ERROR,
                ERROR_MESSAGE
        );
    }
}
