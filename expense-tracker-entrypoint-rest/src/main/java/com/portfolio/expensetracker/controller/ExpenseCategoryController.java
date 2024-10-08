package com.portfolio.expensetracker.controller;

import com.portfolio.expensetracker.api.ExpenseCategoryRestApi;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCategoryCreate;
import com.portfolio.expensetracker.usecases.expensecategory.CreateCategoryUseCase;
import com.portfolio.expensetracker.usecases.expensecategory.DeleteCategoryUseCase;
import com.portfolio.expensetracker.usecases.expensecategory.ListCategoriesUseCase;
import com.portfolio.expensetracker.util.AuthenticationConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ExpenseCategoryController implements ExpenseCategoryRestApi {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final HttpServletRequest httpRequest;

    @Override
    public ResponseEntity<ExpenseCategory> create(ExpenseCategoryCreate expenseCategoryCreate) {
        log.info("Creating expense category: {}.", expenseCategoryCreate);
        String jwt = httpRequest.getHeader(AuthenticationConstants.Authentication.HTTP_HEADER_AUTHORIZATION);

        CreateCategoryUseCase.Input input = CreateCategoryUseCase.Input.builder()
                .jwt(jwt)
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
        String jwt = httpRequest.getHeader(AuthenticationConstants.Authentication.HTTP_HEADER_AUTHORIZATION);

        List<String> idsList = StringUtils.hasText(ids)
                ? List.of(ids.split(","))
                : Collections.emptyList();

        ListCategoriesUseCase.Input input = ListCategoriesUseCase.Input.builder()
                .jwt(jwt)
                .offset(offset)
                .limit(limit)
                .ids(idsList)
                .build();

        ListCategoriesUseCase.Output output = listCategoriesUseCase.execute(input);
        return new ResponseEntity<>(output.getExpenseCategories(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        log.info("Deleting expense category: {}.", id);
        String jwt = httpRequest.getHeader(AuthenticationConstants.Authentication.HTTP_HEADER_AUTHORIZATION);

        DeleteCategoryUseCase.Input input = DeleteCategoryUseCase.Input.builder()
                .jwt(jwt)
                .categoryId(id)
                .build();

        deleteCategoryUseCase.execute(input);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
