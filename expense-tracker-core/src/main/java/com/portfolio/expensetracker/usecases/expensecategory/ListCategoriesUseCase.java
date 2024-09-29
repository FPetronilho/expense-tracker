package com.portfolio.expensetracker.usecases.expensecategory;

import com.portfolio.expensetracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.portfoliomanager.response.AssetResponse;
import com.portfolio.expensetracker.security.context.DigitalUser;
import com.portfolio.expensetracker.util.SecurityUtil;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListCategoriesUseCase {

    private final ExpenseCategoryDataProvider dataProvider;
    private final PortfolioManagerDataProvider portfolioManagerDataProvider;

    public Output execute(Input input) {
        DigitalUser user = SecurityUtil.getDigitalUser();

        List<AssetResponse> assetResponseList = portfolioManagerDataProvider.listAssets(
                input.getJwt(),
                user.getId(),
                input.getOffset(),
                input.getLimit(),
                "com.portfolio",
                "expense-tracker",
                "expense-category",
                null,
                null,
                null
        );

        String assetIds = assetResponseList.stream()
                .map(AssetResponse::getExternalId)
                .toList()
                .toString();

        List<ExpenseCategory> expenseCategories = dataProvider.list(
                input.getOffset(),
                input.getLimit(),
                assetIds
        );

        return Output.builder()
                .expenseCategories(expenseCategories)
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
        private String ids;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {
        private List<ExpenseCategory> expenseCategories;
    }
}
