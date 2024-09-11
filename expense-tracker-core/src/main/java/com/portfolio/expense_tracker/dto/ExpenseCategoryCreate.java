package com.portfolio.expense_tracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.portfolio.expense_tracker.util.Constants;
import com.portfolio.expense_tracker.validation.ValidExpenseCategory;
import jakarta.validation.constraints.NotNull;
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
public class ExpenseCategoryCreate {

    @NotNull(message = Constants.CATEGORY_NAME_MANDATORY_MSG)
    @Pattern(regexp = Constants.CATEGORY_NAME_REGEX, message = Constants.CATEGORY_NAME_INVALID_MSG)
    private String name;

    @NotNull(message = Constants.CATEGORY_DESCRIPTION_MANDATORY_MSG)
    @Pattern(regexp = Constants.CATEGORY_DESCRIPTION_REGEX, message = Constants.CATEGORY_DESCRIPTION_INVALID_MSG)
    private String description;
}
