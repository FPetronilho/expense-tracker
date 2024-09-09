package com.portfolio.expense_tracker.usecases.expense;

import com.portfolio.expense_tracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expense_tracker.domain.Expense;
import com.portfolio.expense_tracker.domain.ExpenseCategory;
import com.portfolio.expense_tracker.domain.OrderBy;
import com.portfolio.expense_tracker.domain.OrderDirection;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListByCriteriaUseCase {

    private final ExpenseDataProvider dataProvider;

    public Output execute(Input input) {
        List<Expense> expenses = dataProvider.listByCriteria(input);
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
        private String categoryName;
        private LocalDateTime date;
        private LocalDateTime from;
        private LocalDateTime to;
        private Float amount;
        private Float amountLte;
        private Float amountGte;
        private List<OrderBy> orderByList;
        private List<OrderDirection> orderDirectionList;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {
        private List<Expense> expenses;
    }
}
