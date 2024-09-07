package com.portfolio.expense_tracker.api;

import com.portfolio.expense_tracker.domain.Expense;
import com.portfolio.expense_tracker.dto.ExpenseCreate;
import com.portfolio.expense_tracker.dto.ExpenseUpdate;
import com.portfolio.expense_tracker.util.ExpenseConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/expenses")
@Validated
public interface ExpenseRestApi {

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Expense> create(@RequestBody @Valid ExpenseCreate expenseCreate);

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Expense> findById(
            @PathVariable @Pattern(regexp = ExpenseConstants.ID_REGEX, message = ExpenseConstants.EXPENSE_ID_INVALID_MSG) String id
    );

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Expense>> findAll(
            @RequestParam(required = false, defaultValue = ExpenseConstants.DEFAULT_OFFSET)
                @Min(value = 0, message = ExpenseConstants.OFFSET_INVALID_MSG) Integer offset,
            @RequestParam(required = false, defaultValue = ExpenseConstants.DEFAULT_LIMIT)
                @Min(value = ExpenseConstants.MIN_LIMIT, message = ExpenseConstants.LIMIT_INVALID_MSG)
                @Max(value = ExpenseConstants.MAX_LIMIT, message = ExpenseConstants.LIMIT_INVALID_MSG) Integer limit
    );

    @PatchMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Expense> update(
            @PathVariable @Pattern(regexp = ExpenseConstants.ID_REGEX, message = ExpenseConstants.EXPENSE_ID_INVALID_MSG) String id,
            @RequestBody @Valid ExpenseUpdate expenseUpdate
            );

    @DeleteMapping(path = "/{id}")
    ResponseEntity<Void> delete(
            @PathVariable @Pattern(regexp = ExpenseConstants.ID_REGEX, message = ExpenseConstants.EXPENSE_ID_INVALID_MSG) String id
    );
}
