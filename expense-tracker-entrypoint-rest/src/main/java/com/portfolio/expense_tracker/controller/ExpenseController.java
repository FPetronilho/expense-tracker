package com.portfolio.expense_tracker.controller;

import com.portfolio.expense_tracker.api.ExpenseRestApi;
import com.portfolio.expense_tracker.domain.Expense;
import com.portfolio.expense_tracker.domain.OrderBy;
import com.portfolio.expense_tracker.domain.OrderDirection;
import com.portfolio.expense_tracker.dto.ExpenseCreate;
import com.portfolio.expense_tracker.dto.ExpenseUpdate;
import com.portfolio.expense_tracker.exception.ParameterValidationFailedException;
import com.portfolio.expense_tracker.usecases.expense.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class ExpenseController implements ExpenseRestApi {

    private final CreateUseCase createUseCase;
    private final FindByIdUseCase findByIdUseCase;
    private final ListByCriteriaUseCase listByCriteriaUseCase;
    private final UpdateUseCase updateUseCase;
    private final DeleteUseCase deleteUseCase;

    @Override
    public ResponseEntity<Expense> create(ExpenseCreate expenseCreate) {
        log.info("Creating expense: {}.", expenseCreate);
        CreateUseCase.Input input = CreateUseCase.Input.builder()
                .expenseCreate(expenseCreate)
                .build();

        CreateUseCase.Output output = createUseCase.execute(input);
        return new ResponseEntity<>(output.getExpense(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Expense> findById(String id) {
        log.info("Finding expense: {}.", id);
        FindByIdUseCase.Input input = FindByIdUseCase.Input.builder()
                .id(id)
                .build();

        FindByIdUseCase.Output output = findByIdUseCase.execute(input);
        return new ResponseEntity<>(output.getExpense(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Expense>> listByCriteria(
            Integer offset,
            Integer limit,
            String categoryName,
            LocalDateTime date,
            LocalDateTime from,
            LocalDateTime to,
            Float amount,
            Float amountGte,
            Float amountLte,
            List<OrderBy> orderByList,
            List<OrderDirection> orderDirectionList
    ) {

        // Input treatment
        if (amount != null) {
            amountGte = null;
            amountLte = null;
        }

        if (amountGte != null || amountLte != null) {
            amount = null;
        }

        if (date != null) {
            from = null;
            to = null;
        }

        if (to != null || from != null) {
            date = null;
        }

        // Input validation
        if (amountGte != null && amountLte != null && amountLte < amountGte) {
            throw new ParameterValidationFailedException(
                    "Invalid amount input: 'amountLte' must be grater than or equal to 'amountGte'."
            );
        }

        if (to != null && from != null && to.isBefore(from)) {
            throw new ParameterValidationFailedException(
                    "Invalid date input: 'to' must be later than 'from'."
            );
        }

        if (orderByList.size() != orderDirectionList.size()) {
            throw new ParameterValidationFailedException(
                    String.format("Invalid 'orderBy' and 'orderDirection' pair. " +
                                    "'orderBy' size is %s and 'orderDirection' size is %s. Both sizes must match.",
                            orderByList.size(),
                            orderDirectionList.size())
            );
        }

        // Method logic
        ListByCriteriaUseCase.Input input = ListByCriteriaUseCase.Input.builder()
                .offset(offset)
                .limit(limit)
                .categoryName(categoryName)
                .date(date)
                .from(from)
                .to(to)
                .amount(amount)
                .amountGte(amountGte)
                .amountLte(amountLte)
                .orderByList(orderByList)
                .orderDirectionList(orderDirectionList)
                .build();

        log.info("Listing expenses by criteria: {}.", input);
        ListByCriteriaUseCase.Output output = listByCriteriaUseCase.execute(input);
        return new ResponseEntity<>(output.getExpenses(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Expense> update(String id, ExpenseUpdate expenseUpdate) {
        log.info("Updating expense: {}. Updated expense data: {}.", id, expenseUpdate);
        UpdateUseCase.Input input = UpdateUseCase.Input.builder()
                .id(id)
                .expenseUpdate(expenseUpdate)
                .build();

        UpdateUseCase.Output output = updateUseCase.execute(input);
        return new ResponseEntity<>(output.getExpense(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        log.info("Deleting expense: {}.", id);
        DeleteUseCase.Input input = DeleteUseCase.Input.builder()
                .id(id)
                .build();

        deleteUseCase.execute(input);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
