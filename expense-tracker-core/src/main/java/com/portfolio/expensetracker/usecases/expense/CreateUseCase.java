package com.portfolio.expensetracker.usecases.expense;

import com.portfolio.expensetracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expensetracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCreate;
import com.portfolio.expensetracker.dto.portfoliomanager.request.AssetRequest;
import com.portfolio.expensetracker.mapper.AssetMapper;
import com.portfolio.expensetracker.security.context.DigitalUser;
import com.portfolio.expensetracker.util.SecurityUtil;
import lombok.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUseCase {

    private final ExpenseDataProvider expenseDataProvider;
    private final ExpenseCategoryDataProvider expenseCategoryDataProvider;
    private final PortfolioManagerDataProvider portfolioManagerDataProvider;

    public Output execute(Input input) {
        // 1. Create expense in DB
        ExpenseCreate expenseCreate = input.getExpenseCreate();
        ExpenseCategory expenseCategory = findExpenseCategoryById(expenseCreate);
        Expense expense = expenseDataProvider.create(expenseCreate, expenseCategory);

        // 2. Create (expense) asset in Portfolio Management
        AssetRequest assetRequest = AssetMapper.toAssetRequest(expense);
        DigitalUser user = SecurityUtil.getDigitalUser();

        try {
            portfolioManagerDataProvider.createAsset(
                    input.getJwt(),
                    user.getId(),
                    assetRequest
            );
        } catch (Exception e) {
            expenseDataProvider.delete(expense.getId());
        }

        return Output.builder()
                .expense(expense)
                .build();
    }

   private ExpenseCategory findExpenseCategoryById(ExpenseCreate expenseCreate) {
        return expenseCategoryDataProvider.findById(expenseCreate.getCategoryId());
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
