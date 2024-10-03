package com.portfolio.expensetracker.mapper;

import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.portfoliomanager.request.AssetRequest;

public class AssetMapper {

    public static AssetRequest toAssetRequest(Expense expense) {
        return AssetRequest.builder()
                .externalId(expense.getId())
                .type("expense")
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

    public static AssetRequest toAssetRequest(ExpenseCategory expenseCategory) {
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
}
