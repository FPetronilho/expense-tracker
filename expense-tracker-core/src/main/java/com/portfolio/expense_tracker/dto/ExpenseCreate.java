package com.portfolio.expense_tracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.portfolio.expense_tracker.util.Constants;
import com.portfolio.expense_tracker.validation.ValidExpenseCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@ValidExpenseCategory
public class ExpenseCreate {

    @NotNull(message = Constants.EXPENSE_CATEGORY_MANDATORY_MSG)
    @Pattern(regexp = Constants.CATEGORY_NAME_REGEX, message = Constants.CATEGORY_NAME_INVALID_MSG)
    private String expenseCategoryName;

    @Pattern(regexp = Constants.EXPENSE_DESCRIPTION_REGEX, message = Constants.EXPENSE_DESCRIPTION_INVALID_MSG)
    private String description;

    @NotNull(message = Constants.EXPENSE_AMOUNT_MANDATORY_MSG)
    @Min(value = Constants.MIN_AMOUNT, message = Constants.EXPENSE_AMOUNT_INVALID_MSG)
    private Float amount;
}
