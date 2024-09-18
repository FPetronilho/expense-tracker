package com.portfolio.expense_tracker.usecases.expensecategory;

import com.portfolio.expense_tracker.dataprovider.ExpenseCategoryDataProvider;
import lombok.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCategoryUseCase {

    private final ExpenseCategoryDataProvider dataProvider;

    public void execute(Input input) {
        dataProvider.delete(input.getCategoryId());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private String categoryId;
    }
}
