package com.portfolio.expensetracker.usecases.expense;

import com.portfolio.expensetracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCreate;
import com.portfolio.expensetracker.dto.portfoliomanager.request.AssetRequest;
import com.portfolio.expensetracker.mapper.AssetMapper;
import com.portfolio.expensetracker.security.context.DigitalUser;
import com.portfolio.expensetracker.usecases.expensecategory.FindCategoryByIdUseCase;
import com.portfolio.expensetracker.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateUseCase {

    private final ExpenseDataProvider expenseDataProvider;
    private final FindCategoryByIdUseCase findExpenseCategoryById;
    private final PortfolioManagerDataProvider portfolioManagerDataProvider;

    public Output execute(Input input) {
        // 1. Create expense in DB
        ExpenseCreate expenseCreate = input.getExpenseCreate();

        // Check if category exists for a specific user
        ExpenseCategory expenseCategory = findExpenseCategoryById.execute(
                FindCategoryByIdUseCase.Input.builder()
                        .jwt(input.getJwt())
                        .id(expenseCreate.getCategoryId())
                        .build()
        ).getExpenseCategory();

        Expense expense = expenseDataProvider.create(expenseCreate, expenseCategory);

        // 2. Create (expense) asset in Portfolio Management
        AssetRequest assetRequest = AssetMapper.toAssetRequest(expense);

        try {
            DigitalUser digitalUser = SecurityUtil.getDigitalUser();
            portfolioManagerDataProvider.createAsset(
                    input.getJwt(),
                    digitalUser.getId(),
                    assetRequest
            );
        } catch (Exception e) {
            log.error("could not create expense in Portfolio Manager. Reason: {}", e.getMessage());
            expenseDataProvider.delete(expense.getId()); // rollback operation
            throw e; // propagate exception to Controller / upper method stack
        }

        return Output.builder()
                .expense(expense)
                .build();
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
