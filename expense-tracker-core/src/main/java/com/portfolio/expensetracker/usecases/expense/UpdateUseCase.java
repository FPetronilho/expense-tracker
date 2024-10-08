package com.portfolio.expensetracker.usecases.expense;

import com.portfolio.expensetracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.dto.ExpenseUpdate;
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
public class UpdateUseCase {

    private final FindByIdUseCase findByIdUseCase;
    private final ExpenseDataProvider expenseDataProvider;

    public Output execute(Input input) {
        AssetResponse asset = findByIdUseCase.execute(
                FindByIdUseCase.Input.builder()
                        .jwt(input.getJwt())
                        .id(input.getId())
                        .build()
        ).getAsset();

        DigitalUser user = SecurityUtil.getDigitalUser();
        if (!AssetResponse.PermissionPolicy.OWNER.equals(asset.getPermissionPolicy())) {
            throw new AuthorizationFailedException(
                    String.format(
                            "Digital user %s cannot update expense %s because it is not its owner",
                            user.getId(),
                            input.getId()
                    )
            );
        }

        Expense expense = expenseDataProvider.update(
                input.getId(),
                input.getExpenseUpdate()
        );

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
        private String id;
        private ExpenseUpdate expenseUpdate;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {
        private Expense expense;
    }
}
