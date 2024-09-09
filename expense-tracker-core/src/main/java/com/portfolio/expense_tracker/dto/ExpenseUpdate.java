package com.portfolio.expense_tracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.portfolio.expense_tracker.domain.ExpenseCategory;
import com.portfolio.expense_tracker.util.Constants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpenseUpdate {

    private ExpenseCategory category;

    @Pattern(regexp = Constants.EXPENSE_DESCRIPTION_REGEX, message = Constants.EXPENSE_DESCRIPTION_INVALID_MSG)
    private String description;

    @Min(value = Constants.MIN_AMOUNT, message = Constants.EXPENSE_AMOUNT_INVALID_MSG)
    private Float amount;
}
