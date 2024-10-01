package com.portfolio.expensetracker.usecases.expensecategory;

import com.portfolio.expensetracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCategoryCreate;
import com.portfolio.expensetracker.dto.portfoliomanager.request.AssetRequest;
import com.portfolio.expensetracker.mapper.AssetMapper;
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
        AssetRequest assetRequest = AssetMapper.toAssetRequest(expenseCategory);
        DigitalUser user = SecurityUtil.getDigitalUser();

        try {
            portfolioManagerDataProvider.createAsset(
                    input.getJwt(),
                    user.getId(),
                    assetRequest
            );
        } catch (Exception e) {
            dataProvider.delete(expenseCategory.getId());
        }

        return Output.builder()
                .expenseCategory(expenseCategory)
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
