package com.portfolio.expense_tracker.controller;

import com.portfolio.expense_tracker.api.ExpenseCategoryRestApi;
import com.portfolio.expense_tracker.domain.ExpenseCategory;
import com.portfolio.expense_tracker.dto.ExpenseCategoryCreate;
import com.portfolio.expense_tracker.usecases.expensecategory.CreateCategoryUseCase;
import com.portfolio.expense_tracker.usecases.expensecategory.DeleteCategoryUseCase;
import com.portfolio.expense_tracker.usecases.expensecategory.ListCategoriesUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ExpenseCategoryController implements ExpenseCategoryRestApi {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    @Override
    public ResponseEntity<ExpenseCategory> create(ExpenseCategoryCreate expenseCategoryCreate) {
        log.info("Creating expense category: {}.", expenseCategoryCreate);
        CreateCategoryUseCase.Input input = CreateCategoryUseCase.Input.builder()
                .expenseCategoryCreate(expenseCategoryCreate)
                .build();

        CreateCategoryUseCase.Output output = createCategoryUseCase.execute(input);
        return new ResponseEntity<>(output.getExpenseCategory(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<ExpenseCategory>> list(Integer offset, Integer limit) {
        log.info("Listing expenses.");
        ListCategoriesUseCase.Input input = ListCategoriesUseCase.Input.builder()
                .offset(offset)
                .limit(limit)
                .build();

        ListCategoriesUseCase.Output output = listCategoriesUseCase.execute(input);
        return new ResponseEntity<>(output.getExpenseCategories(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(String name) {
        log.info("Deleting expense category: {}", name);
        DeleteCategoryUseCase.Input input = DeleteCategoryUseCase.Input.builder()
                .categoryName(name)
                .build();

        deleteCategoryUseCase.execute(input);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
