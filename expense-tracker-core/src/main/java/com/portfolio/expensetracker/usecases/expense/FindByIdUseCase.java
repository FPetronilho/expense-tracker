package com.portfolio.expensetracker.usecases.expense;

import com.portfolio.expensetracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.dto.portfoliomanager.response.AssetResponse;
import com.portfolio.expensetracker.exception.ResourceNotFoundException;
import com.portfolio.expensetracker.security.context.DigitalUser;
import com.portfolio.expensetracker.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindByIdUseCase {

    private final PortfolioManagerDataProvider portfolioManagerDataProvider;
    private final ExpenseDataProvider expenseDataProvider;

    public Output execute(Input input) {
        DigitalUser digitalUser = SecurityUtil.getDigitalUser();

        List<AssetResponse> assetResponseList = portfolioManagerDataProvider.listAssets(
                input.getJwt(),
                digitalUser.getId(),
                Collections.singletonList(input.getId()),
                0,
                1,
                "com.portfolio",
                "expense-tracker",
                "expense",
                null,
                null,
                null
        );

        if (CollectionUtils.isEmpty(assetResponseList)) {
            throw new ResourceNotFoundException(Expense.class, input.getId());
        }

        Expense expense = expenseDataProvider.findById(input.getId());

        return Output.builder()
                .expense(expense)
                .asset(assetResponseList.get(0))
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private String jwt;
        private String id;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {
        private Expense expense;
        private AssetResponse asset;
    }
}
