package com.portfolio.expense_tracker.dataprovider;

import com.portfolio.expense_tracker.domain.ExpenseCategory;

import java.util.List;

public interface ExpenseCategoryDataProvider {

    List<ExpenseCategory> findAll();

    void createAll(List<ExpenseCategory> categories);

    void deleteAll();
}
