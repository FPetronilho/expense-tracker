package com.portfolio.expense_tracker.usecases.expense;

import com.portfolio.expense_tracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expense_tracker.domain.Expense;
import com.portfolio.expense_tracker.dto.ExpenseCreate;
import lombok.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindByIdUseCase {

    private final ExpenseDataProvider dataProvider;

    public Output execute(Input input) {
        Expense expense = dataProvider.findById(input.getId());
        return Output.builder()
                .expense(expense)
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private String id;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {
        private Expense expense;
    }
}
