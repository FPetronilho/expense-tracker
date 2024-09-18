package com.portfolio.expense_tracker.usecases.expensecategory;

import com.portfolio.expense_tracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expense_tracker.domain.ExpenseCategory;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListCategoriesUseCase {

    private final ExpenseCategoryDataProvider dataProvider;

    public Output execute(Input input) {
        List<ExpenseCategory> expenseCategories = dataProvider.list(
                input.getOffset(),
                input.getLimit(),
                input.getIds()
        );

        return Output.builder()
                .expenseCategories(expenseCategories)
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private Integer offset;
        private Integer limit;
        private List<String> ids;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {
        private List<ExpenseCategory> expenseCategories;
    }
}
