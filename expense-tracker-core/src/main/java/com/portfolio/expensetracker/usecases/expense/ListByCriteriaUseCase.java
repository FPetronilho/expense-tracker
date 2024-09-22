package com.portfolio.expensetracker.usecases.expense;

import com.portfolio.expensetracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.domain.OrderBy;
import com.portfolio.expensetracker.domain.OrderDirection;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListByCriteriaUseCase {

    private final PortfolioManagerDataProvider portfolioManagerDataProvider;
    private final ExpenseDataProvider expenseDataProvider;

    public Output execute(Input input) {
        // Call Portfolio Manager to retrieve user assets (expenses)
        portfolioManagerDataProvider.listAssets(); // TODO: replace this random usage by right call

        List<Expense> expenses = expenseDataProvider.listByCriteria(input);
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
        private String categoryId;
        private LocalDateTime date;
        private LocalDateTime from;
        private LocalDateTime to;
        private Float amount;
        private Float amountLte;
        private Float amountGte;
        private List<OrderBy> orderByList;
        private List<OrderDirection> orderDirectionList;
        private List<String> ids;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {
        private List<Expense> expenses;
    }
}
