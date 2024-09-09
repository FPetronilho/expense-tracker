package com.portfolio.expense_tracker.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public enum OrderBy {

    AMOUNT("amount"),
    DATE("date");

    private final String value;
}
