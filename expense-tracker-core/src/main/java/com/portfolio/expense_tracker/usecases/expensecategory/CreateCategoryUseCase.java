package com.portfolio.expense_tracker.usecases.expensecategory;

import com.portfolio.expense_tracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expense_tracker.domain.ExpenseCategory;
import com.portfolio.expense_tracker.dto.ExpenseCategoryCreate;
import lombok.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCategoryUseCase {

    private final ExpenseCategoryDataProvider dataProvider;

    public Output execute(Input input) {
        return Output.builder()
                .expenseCategory(dataProvider.create(input.getExpenseCategoryCreate()))
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private ExpenseCategoryCreate expenseCategoryCreate;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {
        private ExpenseCategory expenseCategory;
    }
}
