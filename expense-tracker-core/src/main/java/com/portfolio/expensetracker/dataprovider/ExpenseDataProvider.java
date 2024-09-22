package com.portfolio.expensetracker.dataprovider;

import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCreate;
import com.portfolio.expensetracker.dto.ExpenseUpdate;
import com.portfolio.expensetracker.usecases.expense.ListByCriteriaUseCase;

import java.util.List;

public interface ExpenseDataProvider {

    Expense create(ExpenseCreate expenseCreate, ExpenseCategory expenseCategory);

    Expense findById(String id);

    List<Expense> listByCriteria(ListByCriteriaUseCase.Input input);

    Expense update(String id, ExpenseUpdate expenseUpdate);

    void delete(String id);
}
