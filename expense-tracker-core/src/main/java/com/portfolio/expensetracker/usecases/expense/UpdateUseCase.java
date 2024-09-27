package com.portfolio.expensetracker.usecases.expense;

import com.portfolio.expensetracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.dto.ExpenseUpdate;
import lombok.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUseCase {

    private final ExpenseDataProvider expenseDataProvider;

    public Output execute(Input input) {
       Expense expense = expenseDataProvider.update(
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