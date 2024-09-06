package com.portfolio.expense_tracker.usecases;

import com.portfolio.expense_tracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expense_tracker.domain.Expense;
import com.portfolio.expense_tracker.dto.ExpenseCreate;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllUseCase {

    private final ExpenseDataProvider dataProvider;

    public Output execute(Input input) {
        List<Expense> expenses = dataProvider.findAll(
                input.getOffset(),
                input.getLimit()
        );
        return Output.builder()
                .expenses(expenses)
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private Integer offset;
        private Integer limit;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {
        private List<Expense> expenses;
    }
}
