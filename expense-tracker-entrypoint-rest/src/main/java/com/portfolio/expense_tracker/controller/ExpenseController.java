package com.portfolio.expense_tracker.controller;

import com.portfolio.expense_tracker.api.ExpenseRestApi;
import com.portfolio.expense_tracker.domain.Expense;
import com.portfolio.expense_tracker.dto.ExpenseCreate;
import com.portfolio.expense_tracker.dto.ExpenseUpdate;
import com.portfolio.expense_tracker.usecases.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class ExpenseController implements ExpenseRestApi {

    private final CreateUseCase createUseCase;
    private final FindByIdUseCase findByIdUseCase;
    private final ListByCriteriaUseCase findAllUseCase;
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
    public ResponseEntity<List<Expense>> findAll(Integer offset, Integer limit) {
        log.info("Finding expenses by criteria");
        ListByCriteriaUseCase.Input input = ListByCriteriaUseCase.Input.builder()
                .offset(offset)
                .limit(limit)
                .build();

        ListByCriteriaUseCase.Output output = findAllUseCase.execute(input);
        return new ResponseEntity<>(output.getExpenses(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Expense> update(String id, ExpenseUpdate expenseUpdate) {
        log.info("Updating expense {}.", id);
        UpdateUseCase.Input input = UpdateUseCase.Input.builder()
                .id(id)
                .expenseUpdate(expenseUpdate)
                .build();

        UpdateUseCase.Output output = updateUseCase.execute(input);
        return new ResponseEntity<>(output.getExpense(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        log.info("Deleting expense {}.", id);
        DeleteUseCase.Input input = DeleteUseCase.Input.builder()
                .id(id)
                .build();

        deleteUseCase.execute(input);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
