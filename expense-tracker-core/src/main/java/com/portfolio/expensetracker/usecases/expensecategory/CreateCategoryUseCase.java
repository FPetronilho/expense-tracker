package com.portfolio.expensetracker.usecases.expensecategory;

import com.portfolio.expensetracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCategoryCreate;
import com.portfolio.expensetracker.dto.portfoliomanager.request.AssetRequest;
import lombok.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCategoryUseCase {

    private final ExpenseCategoryDataProvider dataProvider;
    private final PortfolioManagerDataProvider portfolioManagerDataProvider;

    public Output execute(Input input) {
        ExpenseCategoryCreate expenseCategoryCreate = input.getExpenseCategoryCreate();
        ExpenseCategory expenseCategory = dataProvider.create(expenseCategoryCreate);

        AssetRequest assetRequest = mapExpenseCategoryToAssetRequest(expenseCategory);
        portfolioManagerDataProvider.createAsset(assetRequest);

        return Output.builder()
                .expenseCategory(expenseCategory)
                .build();
    }

    private AssetRequest mapExpenseCategoryToAssetRequest(ExpenseCategory expenseCategory) {
        return AssetRequest.builder()
                .externalId(expenseCategory.getId())
                .type("expense-category")
                .artifactInfo(
                        new AssetRequest.ArtifactInformation(
                                "com.portfolio",
                                "expense-tracker",
                                "0.0.1-SNAPSHOT"
                        )
                )
                .permissionPolicy(AssetRequest.PermissionPolicy.OWNER)
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private ExpenseCategoryCreate expenseCategoryCreate;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {
        private ExpenseCategory expenseCategory;
    }
}
