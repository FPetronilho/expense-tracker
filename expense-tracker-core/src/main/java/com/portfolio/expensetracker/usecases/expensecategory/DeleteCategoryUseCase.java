package com.portfolio.expensetracker.usecases.expensecategory;

import com.portfolio.expensetracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.exception.InternalServerErrorException;
import lombok.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCategoryUseCase {

    private final ExpenseCategoryDataProvider dataProvider;
    private final PortfolioManagerDataProvider portfolioManagerDataProvider;

    public void execute(Input input) {
        try {
            portfolioManagerDataProvider.deleteAsset(input.getJwt(), input.getCategoryId());
        } catch (Exception e) {
            throw new InternalServerErrorException("Asset could not be deleted in portfolio manager. Please try again.");
        }

        dataProvider.delete(input.getCategoryId());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private String jwt;
        private String categoryId;
    }
}
