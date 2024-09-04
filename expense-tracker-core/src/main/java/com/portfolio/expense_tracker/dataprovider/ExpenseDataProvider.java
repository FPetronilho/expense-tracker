package com.portfolio.expense_tracker.dataprovider;

import com.portfolio.expense_tracker.domain.Expense;
import com.portfolio.expense_tracker.dto.ExpenseCreate;
import com.portfolio.expense_tracker.dto.ExpenseUpdate;

import java.util.List;

public interface ExpenseDataProvider {

    Expense create(ExpenseCreate expenseCreate);

    Expense findById(String id);

    List<Expense> findAll(
            Integer offset,
            Integer limit
    );

    Expense update(
            String id,
            ExpenseUpdate expenseUpdate
    );

    void delete(String id);
}
