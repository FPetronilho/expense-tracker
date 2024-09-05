package com.portfolio.expense_tracker.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public enum OrderDirection {

    ASC("ascending"),
    DESC("descending");

    private final String value;
}
