package com.portfolio.expensetracker.dataprovider;

import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCategoryCreate;

import java.util.List;

public interface ExpenseCategoryDataProvider {

    ExpenseCategory create(ExpenseCategoryCreate expenseCategoryCreate);

    List<ExpenseCategory> list(Integer offset, Integer limit, String ids);

    void delete(String id);

    ExpenseCategory findById(String id);
}
