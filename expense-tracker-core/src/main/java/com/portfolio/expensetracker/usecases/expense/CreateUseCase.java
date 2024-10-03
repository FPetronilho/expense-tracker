package com.portfolio.expensetracker.usecases.expense;

import com.portfolio.expensetracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expensetracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCreate;
import com.portfolio.expensetracker.dto.portfoliomanager.request.AssetRequest;
import com.portfolio.expensetracker.dto.portfoliomanager.response.AssetResponse;
import com.portfolio.expensetracker.exception.ResourceNotFoundException;
import com.portfolio.expensetracker.mapper.AssetMapper;
import com.portfolio.expensetracker.security.context.DigitalUser;
import com.portfolio.expensetracker.util.Constants;
import com.portfolio.expensetracker.util.SecurityUtil;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateUseCase {

    private final ExpenseDataProvider expenseDataProvider;
    private final ExpenseCategoryDataProvider expenseCategoryDataProvider;
    private final PortfolioManagerDataProvider portfolioManagerDataProvider;

    public Output execute(Input input) {
        // 1. Create expense in DB
        ExpenseCreate expenseCreate = input.getExpenseCreate();

        // Check if category exists for a specific user
        DigitalUser digitalUser = SecurityUtil.getDigitalUser();
        ExpenseCategory expenseCategory = findExpenseCategoryById(
                input.getJwt(),
                digitalUser,
                expenseCreate.getCategoryId()
        );

        Expense expense = expenseDataProvider.create(expenseCreate, expenseCategory);

        // 2. Create (expense) asset in Portfolio Management
        AssetRequest assetRequest = AssetMapper.toAssetRequest(expense);

        try {
            portfolioManagerDataProvider.createAsset(
                    input.getJwt(),
                    digitalUser.getId(),
                    assetRequest
            );
        } catch (Exception e) {
            expenseDataProvider.delete(expense.getId());
        }

        return Output.builder()
                .expense(expense)
                .build();
    }

    private ExpenseCategory findExpenseCategoryById(
            String jwt,
            DigitalUser digitalUser,
            String categoryId
    ) {
        List<AssetResponse> assetResponseList = portfolioManagerDataProvider.listAssets(
                jwt,
                digitalUser.getId(),
                Integer.valueOf(Constants.DEFAULT_OFFSET),
                Constants.MAX_LIMIT,
                "com.portfolio",
                "expense-tracker",
                "expense-category",
                null,
                null,
                null
        );

        List<String> assetIds = assetResponseList.stream()
                .map(AssetResponse::getExternalId)
                .toList();

        if (assetIds.contains(categoryId)) {
            return expenseCategoryDataProvider.findById(categoryId);
        }

        throw new ResourceNotFoundException(ExpenseCategory.class, categoryId);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private String jwt;
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
