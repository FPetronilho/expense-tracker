package com.portfolio.expense_tracker.dataprovider;

import com.portfolio.expense_tracker.domain.Expense;
import com.portfolio.expense_tracker.dto.ExpenseCreate;
import com.portfolio.expense_tracker.dto.ExpenseUpdate;
import com.portfolio.expense_tracker.usecases.ListByCriteriaUseCase;

import java.util.List;

public interface ExpenseDataProvider {

    Expense create(ExpenseCreate expenseCreate);

    Expense findById(String id);

    List<Expense> listByCriteria(ListByCriteriaUseCase.Input input);

    Expense update(
            String id,
            ExpenseUpdate expenseUpdate
    );

    void delete(String id);
}
