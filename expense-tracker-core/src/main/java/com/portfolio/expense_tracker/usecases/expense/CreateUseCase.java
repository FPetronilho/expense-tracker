package com.portfolio.expense_tracker.usecases.expense;

import com.portfolio.expense_tracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expense_tracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expense_tracker.domain.Expense;
import com.portfolio.expense_tracker.domain.ExpenseCategory;
import com.portfolio.expense_tracker.dto.ExpenseCreate;
import lombok.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUseCase {

    private final ExpenseDataProvider expenseDataProvider;
    private final ExpenseCategoryDataProvider expenseCategoryDataProvider;

    public Output execute(Input input) {
        ExpenseCreate expenseCreate = input.getExpenseCreate();
        ExpenseCategory expenseCategory = findExpenseCategoryById(expenseCreate);

        Expense expense = expenseDataProvider.create(expenseCreate, expenseCategory);
        return Output.builder()
                .expense(expense)
                .build();
    }

   private ExpenseCategory findExpenseCategoryById(ExpenseCreate expenseCreate) {
        return expenseCategoryDataProvider.findById(expenseCreate.getCategoryId());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private ExpenseCreate expenseCreate;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {
        private Expense expense;
    }
}
