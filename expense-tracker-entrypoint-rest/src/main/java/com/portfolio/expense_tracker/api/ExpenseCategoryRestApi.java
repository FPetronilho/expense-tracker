package com.portfolio.expense_tracker.api;

import com.portfolio.expense_tracker.domain.ExpenseCategory;
import com.portfolio.expense_tracker.dto.ExpenseCategoryCreate;
import com.portfolio.expense_tracker.util.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/expenseCategories")
@Validated
public interface ExpenseCategoryRestApi {

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<ExpenseCategory> create(@RequestBody @Valid ExpenseCategoryCreate expenseCategoryCreate);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<ExpenseCategory>> list(
            @RequestParam(required = false, defaultValue = Constants.DEFAULT_OFFSET)
                @Min(value = Constants.MIN_OFFSET, message = Constants.OFFSET_INVALID_MSG) Integer offset,

            @RequestParam(required = false, defaultValue = Constants.DEFAULT_LIMIT)
                @Min(value = Constants.MIN_LIMIT, message = Constants.LIMIT_INVALID_MSG)
                @Max(value = Constants.MAX_LIMIT, message = Constants.LIMIT_INVALID_MSG) Integer limit,

            @RequestParam(required = false) List<String> ids
    );

    @DeleteMapping(path = "/{name}")
    ResponseEntity<Void> delete(
            @PathVariable
            @Pattern(regexp = Constants.CATEGORY_NAME_REGEX, message = Constants.CATEGORY_NAME_INVALID_MSG) String name
    );
}
