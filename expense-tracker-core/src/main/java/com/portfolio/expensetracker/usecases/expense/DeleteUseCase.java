package com.portfolio.expensetracker.usecases.expense;

import com.portfolio.expensetracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.exception.InternalServerErrorException;
import com.portfolio.expensetracker.security.context.DigitalUser;
import com.portfolio.expensetracker.util.SecurityUtil;
import lombok.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUseCase {

    private final ExpenseDataProvider dataProvider;
    private final PortfolioManagerDataProvider portfolioManagerDataProvider;

    public void execute(Input input) {
        try {
            portfolioManagerDataProvider.deleteAsset(input.getJwt(), input.getId());
        } catch (Exception e) {
            throw new InternalServerErrorException("Asset could not be deleted in portfolio manager. Please try again.");
        }

        dataProvider.delete(input.getId());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private String jwt;
        private String id;
    }
}
