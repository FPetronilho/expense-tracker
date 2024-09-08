package com.portfolio.expense_tracker.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public enum Environment {

    LOCAL("local"),
    DEVELOPMENT("dev"),
    SYSTEM_ACCEPTANCE_TEST("sat"),
    PRE_PRODUCTION("pre"),
    PRODUCTION("prod");

    private final String value;
}
