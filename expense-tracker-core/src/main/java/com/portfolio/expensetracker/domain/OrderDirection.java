package com.portfolio.expensetracker.domain;

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
