package com.portfolio.expense_tracker.usecases.expense;

import com.portfolio.expense_tracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expense_tracker.domain.Expense;
import com.portfolio.expense_tracker.dto.ExpenseCreate;
import com.portfolio.expense_tracker.dto.ExpenseUpdate;
import lombok.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUseCase {

    private final ExpenseDataProvider dataProvider;

    public Output execute(Input input) {
        Expense expense = dataProvider.update(
                input.getId(),
                input.getExpenseUpdate()
        );

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
        private ExpenseUpdate expenseUpdate;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {
        private Expense expense;
    }
}
