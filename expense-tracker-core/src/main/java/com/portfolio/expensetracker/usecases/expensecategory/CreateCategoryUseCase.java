package com.portfolio.expensetracker.usecases.expensecategory;

import com.portfolio.expensetracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCategoryCreate;
import com.portfolio.expensetracker.dto.portfoliomanager.request.AssetRequest;
import com.portfolio.expensetracker.security.context.DigitalUser;
import com.portfolio.expensetracker.util.SecurityUtil;
import lombok.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCategoryUseCase {

    private final ExpenseCategoryDataProvider dataProvider;
    private final PortfolioManagerDataProvider portfolioManagerDataProvider;

    public Output execute(Input input) {
        // 1. Create expense in DB
        ExpenseCategoryCreate expenseCategoryCreate = input.getExpenseCategoryCreate();
        ExpenseCategory expenseCategory = dataProvider.create(expenseCategoryCreate);

        // 2. Create (expense-category) asset in Portfolio Management
        // TODO: if asset could not be created in PM, rollback the insert in DB.
        // TIP: add a try catch surrounding createAsset()
        AssetRequest assetRequest = toAssetRequest(expenseCategory);
        DigitalUser user = SecurityUtil.getDigitalUser();
        portfolioManagerDataProvider.createAsset(
                input.getJwt(),
                user.getId(),
                assetRequest
        );

        return Output.builder()
                .expenseCategory(expenseCategory)
                .build();
    }

    // TODO: move this method to a Mapper
    private AssetRequest toAssetRequest(ExpenseCategory expenseCategory) {
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
        private String jwt;
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
