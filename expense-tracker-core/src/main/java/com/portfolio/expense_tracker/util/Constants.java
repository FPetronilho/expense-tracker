package com.portfolio.expense_tracker.util;

public class Constants {

    // Default values
    public static final String DEFAULT_OFFSET = "0";
    public static final String DEFAULT_LIMIT = "10";
    public static final int MIN_OFFSET = 0;
    public static final int MIN_LIMIT = 1;
    public static final int MAX_LIMIT = 100;
    public static final int MIN_AMOUNT = 0;
    public static final String DEFAULT_ORDER = "DATE";
    public static final String DEFAULT_DIRECTION = "ASC";


    // Database
    public static final String CREATED_AT_DB_FIELD = "createdAt";


    // Required fields validation
    public static final String EXPENSE_CATEGORY_ID_MANDATORY_MSG = "'categoryId' is mandatory.";
    public static final String EXPENSE_DESCRIPTION_MANDATORY_MSG = "'description' is mandatory.";
    public static final String EXPENSE_AMOUNT_MANDATORY_MSG = "'amount' is mandatory.";
    public static final String CATEGORY_NAME_MANDATORY_MSG = "'name' is mandatory.";
    public static final String CATEGORY_DESCRIPTION_MANDATORY_MSG = "'description' is mandatory.";


    // Regex
    public static final String EXPENSE_DESCRIPTION_REGEX = "[ \\wÀ-ú\\.:,;\\-\\[\\]()]{1,100}";
    public static final String CATEGORY_DESCRIPTION_REGEX = "[ \\wÀ-ú\\.:,;\\-\\[\\]()]{1,100}";
    public static final String CATEGORY_NAME_REGEX = "[ \\wÀ-ú\\.:,;\\-\\[\\]()]{1,30}";
    public static final String ID_REGEX = "[a-fA-F\\d\\-]{36}";


    // Fields validation
    public static final String EXPENSE_AMOUNT_INVALID_MSG = "'amount' must be positive.";
    public static final String EXPENSE_AMOUNT_GTE_INVALID_MSG = "'amountGte' must be positive.";
    public static final String EXPENSE_AMOUNT_LTE_INVALID_MSG = "'amountLte' must be positive.";
    public static final String EXPENSE_DESCRIPTION_INVALID_MSG = "'description' must match: " + EXPENSE_DESCRIPTION_REGEX + ".";
    public static final String EXPENSE_ID_INVALID_MSG = "'id' must match: " + ID_REGEX + ".";
    public static final String OFFSET_INVALID_MSG = "'offset' must be positive";
    public static final String LIMIT_INVALID_MSG = "'limit' must be in the range [" + MIN_LIMIT + ", " + MAX_LIMIT + "]";
    public static final String CATEGORY_NAME_INVALID_MSG = "'name' must match: " + CATEGORY_NAME_REGEX + ".";
    public static final String CATEGORY_ID_INVALID_MSG = "'categoryId' must match: " + ID_REGEX + ".";
    public static final String CATEGORY_DESCRIPTION_INVALID_MSG = "'description' must match: " + CATEGORY_DESCRIPTION_REGEX + ".";
}
