package com.portfolio.expensetracker.api;

import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.domain.OrderBy;
import com.portfolio.expensetracker.domain.OrderDirection;
import com.portfolio.expensetracker.dto.ExpenseCreate;
import com.portfolio.expensetracker.dto.ExpenseUpdate;
import com.portfolio.expensetracker.util.Constants;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
            @PathVariable @Pattern(regexp = Constants.ID_REGEX, message = Constants.EXPENSE_ID_INVALID_MSG) String id
    );

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Expense>> listByCriteria(
            @RequestParam(required = false, defaultValue = Constants.DEFAULT_OFFSET)
                @Min(value = Constants.MIN_OFFSET, message = Constants.OFFSET_INVALID_MSG) Integer offset,

            @RequestParam(required = false, defaultValue = Constants.DEFAULT_LIMIT)
                @Min(value = Constants.MIN_LIMIT, message = Constants.LIMIT_INVALID_MSG)
                @Max(value = Constants.MAX_LIMIT, message = Constants.LIMIT_INVALID_MSG) Integer limit,

            @RequestParam(required = false)
                @Pattern(regexp = Constants.ID_REGEX,
                        message = Constants.CATEGORY_ID_INVALID_MSG) String categoryId,

            @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,

            @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,

            @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,

            @RequestParam(required = false)
                @Min(value = Constants.MIN_AMOUNT, message = Constants.EXPENSE_AMOUNT_INVALID_MSG) Float amount,

            @RequestParam(required = false)
                @Min(value = Constants.MIN_AMOUNT, message = Constants.EXPENSE_AMOUNT_GTE_INVALID_MSG) Float amountGte,

            @RequestParam(required = false)
                @Min(value = Constants.MIN_AMOUNT, message = Constants.EXPENSE_AMOUNT_LTE_INVALID_MSG) Float amountLte,

            @RequestParam(required = false, defaultValue = Constants.DEFAULT_ORDER) List<OrderBy> orderByList,

            @RequestParam(required = false, defaultValue = Constants.DEFAULT_DIRECTION) List<OrderDirection> orderDirectionList,

            @RequestParam(required = false)
                @Pattern(regexp = Constants.ID_LIST_REGEX, message = Constants.IDS_INVALID_MSG) String ids
    );

    @PatchMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Expense> update(
            @PathVariable @Pattern(regexp = Constants.ID_REGEX, message = Constants.EXPENSE_ID_INVALID_MSG) String id,
            @RequestBody @Valid ExpenseUpdate expenseUpdate
    );

    @DeleteMapping(path = "/{id}")
    ResponseEntity<Void> delete(
            @PathVariable @Pattern(regexp = Constants.ID_REGEX, message = Constants.EXPENSE_ID_INVALID_MSG) String id
    );
}
