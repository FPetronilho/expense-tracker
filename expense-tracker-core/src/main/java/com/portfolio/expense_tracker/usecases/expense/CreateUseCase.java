package com.portfolio.expense_tracker.usecases.expense;

import com.portfolio.expense_tracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expense_tracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expense_tracker.domain.Expense;
import com.portfolio.expense_tracker.domain.ExpenseCategory;
import com.portfolio.expense_tracker.dto.ExpenseCreate;
import com.portfolio.expense_tracker.exception.ParameterValidationFailedException;
import com.portfolio.expense_tracker.util.Constants;
import lombok.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUseCase {

    private final ExpenseDataProvider expenseDataProvider;
    private final ExpenseCategoryDataProvider expenseCategoryDataProvider;

    public Output execute(Input input) {
        ExpenseCreate expenseCreate = input.getExpenseCreate();
        ExpenseCategory expenseCategory = findOrCreateExpenseCategory(expenseCreate);

        Expense expense = expenseDataProvider.create(expenseCreate, expenseCategory);
        return Output.builder()
                .expense(expense)
                .build();
    }

    private ExpenseCategory findOrCreateExpenseCategory(ExpenseCreate expenseCreate) {
        // Check if category already exists based on category name provided
        if (expenseCreate.getExpenseCategoryName() != null) {
            ExpenseCategory expenseCategory = expenseCategoryDataProvider.findByName(expenseCreate.getExpenseCategoryName());

            if (expenseCategory != null) {
                return expenseCategory;
            }
        }

        // If category does not exit yet, use provided category object to create a new one
        if (expenseCreate.getCategory() != null) {
            return expenseCategoryDataProvider.create(expenseCreate.getCategory());
        }

        // Code should never get to this point
        throw new ParameterValidationFailedException(Constants.EXPENSE_CATEGORY_VALIDATOR_MSG);
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
