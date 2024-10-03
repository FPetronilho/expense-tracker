package com.portfolio.expensetracker.usecases.expense;

import com.portfolio.expensetracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.domain.OrderBy;
import com.portfolio.expensetracker.domain.OrderDirection;
import com.portfolio.expensetracker.dto.portfoliomanager.response.AssetResponse;
import com.portfolio.expensetracker.security.context.DigitalUser;
import com.portfolio.expensetracker.util.SecurityUtil;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListByCriteriaUseCase {

    private final PortfolioManagerDataProvider portfolioManagerDataProvider;
    private final ExpenseDataProvider expenseDataProvider;

    public Output execute(Input input) {
        /*
            Retrieve Digital User from Security Context that was previously retrieved from the JWT.
            It is retrieved from the JWT in the Authentication Filter class.
         */
        DigitalUser user = SecurityUtil.getDigitalUser();

        // Call Portfolio Manager to retrieve user assets (expenses)
        List<AssetResponse> assetResponseList = portfolioManagerDataProvider.listAssets(
                input.getJwt(),
                user.getId(),
                input.getOffset(),
                input.getLimit(),
                "com.portfolio",
                "expense-tracker",
                "expense",
                input.getFrom(),
                input.getDate(),
                input.getTo()
        );

        String assetIds = assetResponseList.stream()
                .map(AssetResponse::getExternalId)
                .collect(Collectors.joining(","));

        if (assetIds.isEmpty()) {
            assetIds = " ";
        }

        input.setIds(assetIds);
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
        private String jwt;
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
        private String ids;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {
        private List<Expense> expenses;
    }
}
