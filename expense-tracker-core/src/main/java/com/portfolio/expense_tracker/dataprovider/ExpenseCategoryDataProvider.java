package com.portfolio.expense_tracker.dataprovider;

import com.portfolio.expense_tracker.domain.ExpenseCategory;
import com.portfolio.expense_tracker.dto.ExpenseCategoryCreate;

import java.util.List;

public interface ExpenseCategoryDataProvider {

    ExpenseCategory create(ExpenseCategoryCreate expenseCategoryCreate);

    List<ExpenseCategory> list(Integer offset, Integer limit);

    void delete(String name);

    ExpenseCategory findByName(String name);
}
