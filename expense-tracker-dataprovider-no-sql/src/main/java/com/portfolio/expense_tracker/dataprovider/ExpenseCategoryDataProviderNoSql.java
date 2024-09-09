package com.portfolio.expense_tracker.dataprovider;

import com.portfolio.expense_tracker.domain.ExpenseCategory;
import com.portfolio.expense_tracker.dto.ExpenseCategoryCreate;
import com.portfolio.expense_tracker.mapper.ExpenseMapperDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryDataProviderNoSql implements ExpenseCategoryDataProvider{

    private final ExpenseMapperDataProvider mapper;

    @Override
    public ExpenseCategory create(ExpenseCategoryCreate expenseCategoryCreate) {
        return null;
    }

    @Override
    public List<ExpenseCategory> list(Integer offset, Integer limit) {
        return null;
    }

    @Override
    public void delete(String name) {

    }
}
