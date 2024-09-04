package com.portfolio.expense_tracker.util;

public class Constants {

    // Required fields validation
    public static final String EXPENSE_CATEGORY_MANDATORY_MSG = "'category' is mandatory.";
    public static final String EXPENSE_AMOUNT_MANDATORY_MSG = "'amount' is mandatory.";


    // Regex
    public static final String EXPENSE_DESCRIPTION_REGEX = "[ \\wÀ-ú\\.:,;\\-\\[\\]()]{1,100}";
    public static final String ID_REGEX = "[a-fA-F\\d\\-]{36}";


    // Fields validation
    public static final String EXPENSE_AMOUNT_INVALID_MSG = "'amount' must be positive.";
    public static final String EXPENSE_DESCRIPTION_INVALID_MSG = "'description' must match: " + EXPENSE_DESCRIPTION_REGEX + ".";
}
