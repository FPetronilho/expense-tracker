package com.portfolio.expense_tracker.usecases;

import com.portfolio.expense_tracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expense_tracker.domain.Expense;
import com.portfolio.expense_tracker.dto.ExpenseCreate;
import lombok.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUseCase {

    private final ExpenseDataProvider dataProvider;

    public void execute(Input input) {
        dataProvider.delete(input.getId());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private String id;
    }
}
