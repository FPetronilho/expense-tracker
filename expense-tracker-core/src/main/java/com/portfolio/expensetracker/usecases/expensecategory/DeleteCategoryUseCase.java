package com.portfolio.expensetracker.usecases.expensecategory;

import com.portfolio.expensetracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.dto.portfoliomanager.response.AssetResponse;
import com.portfolio.expensetracker.exception.AuthorizationFailedException;
import com.portfolio.expensetracker.security.context.DigitalUser;
import com.portfolio.expensetracker.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCategoryUseCase {

    private final FindCategoryByIdUseCase findByIdUseCase;
    private final ExpenseCategoryDataProvider dataProvider;
    private final PortfolioManagerDataProvider portfolioManagerDataProvider;

    public void execute(Input input) {
        AssetResponse asset = findByIdUseCase.execute(
                FindCategoryByIdUseCase.Input.builder()
                        .jwt(input.getJwt())
                        .id(input.getCategoryId())
                        .build()
        ).getAsset();

        DigitalUser user = SecurityUtil.getDigitalUser();
        if (!AssetResponse.PermissionPolicy.OWNER.equals(asset.getPermissionPolicy())) {
            throw new AuthorizationFailedException(
                    String.format(
                            "Digital user %s can not delete expense category %s because it is not its owner",
                            user.getId(),
                            input.getCategoryId()
                    )
            );
        }

        portfolioManagerDataProvider.deleteAsset(input.getJwt(), user.getId(), input.getCategoryId());
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
