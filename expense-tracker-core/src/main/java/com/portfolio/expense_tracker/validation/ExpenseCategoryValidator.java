package com.portfolio.expense_tracker.validation;

import com.portfolio.expense_tracker.dto.ExpenseCreate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExpenseCategoryValidator implements ConstraintValidator<ValidExpenseCategory, ExpenseCreate> {

    @Override
    public boolean isValid(ExpenseCreate expenseCreate, ConstraintValidatorContext constraintValidatorContext) {
        return expenseCreate.getCategory() != null || expenseCreate.getExpenseCategoryName() != null &&
                !expenseCreate.getExpenseCategoryName().isBlank();
    }
}
