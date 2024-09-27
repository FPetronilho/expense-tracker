package com.portfolio.expensetracker.usecases.expense;

import com.portfolio.expensetracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expensetracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCreate;
import com.portfolio.expensetracker.dto.portfoliomanager.request.AssetRequest;
import lombok.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUseCase {

    private final ExpenseDataProvider expenseDataProvider;
    private final ExpenseCategoryDataProvider expenseCategoryDataProvider;
    private final PortfolioManagerDataProvider portfolioManagerDataProvider;

    public Output execute(Input input) {
        ExpenseCreate expenseCreate = input.getExpenseCreate();
        ExpenseCategory expenseCategory = findExpenseCategoryById(expenseCreate);

        Expense expense = expenseDataProvider.create(expenseCreate, expenseCategory);
        AssetRequest assetRequest = mapExpenseToAssetRequest(expense);
        portfolioManagerDataProvider.createAsset(assetRequest);

        return Output.builder()
                .expense(expense)
                .build();
    }

   private ExpenseCategory findExpenseCategoryById(ExpenseCreate expenseCreate) {
        return expenseCategoryDataProvider.findById(expenseCreate.getCategoryId());
    }

    private AssetRequest mapExpenseToAssetRequest(Expense expense) {
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

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private ExpenseCreate expenseCreate;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {
        private Expense expense;
    }
}
