package com.portfolio.expensetracker.usecases.expensecategory;

import com.portfolio.expensetracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCategoryCreate;
import com.portfolio.expensetracker.dto.portfoliomanager.request.AssetRequest;
import com.portfolio.expensetracker.exception.ResourceAlreadyExistsException;
import com.portfolio.expensetracker.mapper.AssetMapper;
import com.portfolio.expensetracker.security.context.DigitalUser;
import com.portfolio.expensetracker.util.Constants;
import com.portfolio.expensetracker.util.SecurityUtil;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateCategoryUseCase {

    private final ExpenseCategoryDataProvider dataProvider;
    private final PortfolioManagerDataProvider portfolioManagerDataProvider;
    private final ListCategoriesUseCase listCategoriesUseCase;

    public Output execute(Input input) {
        // 1. Check if category already exists in portfolio manager for a specific user
        DigitalUser digitalUser = SecurityUtil.getDigitalUser();
        ExpenseCategoryCreate expenseCategoryCreate = input.getExpenseCategoryCreate();

        if (findExpenseCategoryByName(input.getJwt(), digitalUser, expenseCategoryCreate.getName())) {
            throw new ResourceAlreadyExistsException(ExpenseCategory.class, expenseCategoryCreate.getName());
        }

        // 2. Create expense in DB
        ExpenseCategory expenseCategory = dataProvider.create(expenseCategoryCreate);

        // 3. Create (expense-category) asset in Portfolio Management
        AssetRequest assetRequest = AssetMapper.toAssetRequest(expenseCategory);

        try {
            portfolioManagerDataProvider.createAsset(
                    input.getJwt(),
                    digitalUser.getId(),
                    assetRequest
            );
        } catch (Exception e) {
            dataProvider.delete(expenseCategory.getId());
        }

        return Output.builder()
                .expenseCategory(expenseCategory)
                .build();
    }

    // TODO: Decide whether this check should be done or, if the user can have multiple categories with the same name
    private boolean findExpenseCategoryByName(
            String jwt,
            DigitalUser digitalUser,
            String categoryName
    ) {
        ListCategoriesUseCase.Input input = ListCategoriesUseCase.Input.builder()
                .jwt(jwt)
                .offset(Integer.valueOf(Constants.DEFAULT_OFFSET))
                .limit(Constants.MAX_LIMIT)
                .build();

        ListCategoriesUseCase.Output output = listCategoriesUseCase.execute(input);
        List<ExpenseCategory> expenseCategories = output.getExpenseCategories();
        List<String> categoryNames = expenseCategories.stream()
                .map(ExpenseCategory::getName)
                .toList();

        if (categoryNames.contains(categoryName)) {
            return true;
        }

        return false;
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
