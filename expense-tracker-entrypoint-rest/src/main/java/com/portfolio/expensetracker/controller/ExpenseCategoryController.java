package com.portfolio.expensetracker.controller;

import com.portfolio.expensetracker.api.ExpenseCategoryRestApi;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCategoryCreate;
import com.portfolio.expensetracker.usecases.expensecategory.CreateCategoryUseCase;
import com.portfolio.expensetracker.usecases.expensecategory.DeleteCategoryUseCase;
import com.portfolio.expensetracker.usecases.expensecategory.ListCategoriesUseCase;
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
    public ResponseEntity<List<ExpenseCategory>> list(
            Integer offset,
            Integer limit,
            String ids
    ) {

        log.info("Listing expenses: (offset={}, limit={}, ids={}.)", offset, limit, ids);
        ListCategoriesUseCase.Input input = ListCategoriesUseCase.Input.builder()
                .offset(offset)
                .limit(limit)
                .ids(ids)
                .build();

        ListCategoriesUseCase.Output output = listCategoriesUseCase.execute(input);
        return new ResponseEntity<>(output.getExpenseCategories(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        log.info("Deleting expense category: {}.", id);
        DeleteCategoryUseCase.Input input = DeleteCategoryUseCase.Input.builder()
                .categoryId(id)
                .build();

        deleteCategoryUseCase.execute(input);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}