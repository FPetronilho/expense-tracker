package com.portfolio.expensetracker.usecases.expensecategory;

import com.portfolio.expensetracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.portfoliomanager.response.AssetResponse;
import com.portfolio.expensetracker.exception.ResourceNotFoundException;
import com.portfolio.expensetracker.security.context.DigitalUser;
import com.portfolio.expensetracker.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindCategoryByIdUseCase {

    private final PortfolioManagerDataProvider portfolioManagerDataProvider;
    private final ExpenseCategoryDataProvider expenseCategoryDataProvider;

    public Output execute(Input input) {
        DigitalUser digitalUser = SecurityUtil.getDigitalUser();

        List<AssetResponse> assetResponseList = portfolioManagerDataProvider.listAssets(
                input.getJwt(),
                digitalUser.getId(),
                Collections.singletonList(input.getId()),
                0,
                1,
                "com.portfolio",
                "expense-tracker",
                "expense-category",
                null,
                null,
                null
        );

        if (CollectionUtils.isEmpty(assetResponseList)) {
            throw new ResourceNotFoundException(ExpenseCategory.class, input.getId());
        }

        ExpenseCategory expenseCategory = expenseCategoryDataProvider.findById(input.getId());

        return Output.builder()
                .expenseCategory(expenseCategory)
                .asset(assetResponseList.get(0))
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private String jwt;
        private String id;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {
        private ExpenseCategory expenseCategory;
        private AssetResponse asset;
    }

}
