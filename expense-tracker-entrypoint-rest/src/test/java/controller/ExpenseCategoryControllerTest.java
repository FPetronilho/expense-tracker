package controller;

import com.portfolio.expensetracker.controller.ExpenseCategoryController;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCategoryCreate;
import com.portfolio.expensetracker.usecases.expensecategory.CreateCategoryUseCase;
import com.portfolio.expensetracker.usecases.expensecategory.DeleteCategoryUseCase;
import com.portfolio.expensetracker.usecases.expensecategory.FindCategoryByIdUseCase;
import com.portfolio.expensetracker.usecases.expensecategory.ListCategoriesUseCase;
import com.portfolio.expensetracker.util.AuthenticationConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExpenseCategoryControllerTest {

    @Mock
    private CreateCategoryUseCase createCategoryUseCase;

    @Mock
    private FindCategoryByIdUseCase findCategoryByIdUseCase;

    @Mock
    private ListCategoriesUseCase listCategoriesUseCase;

    @Mock
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private ExpenseCategoryController expenseCategoryController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_shouldReturnExpenseCategoryAndHttpStatusCreated() {
        // Given
        ExpenseCategoryCreate expenseCategoryCreate = new ExpenseCategoryCreate();

        ExpenseCategory expectedExpenseCategory = new ExpenseCategory();
        String jwt = "test-jwt";

        when(httpServletRequest.getHeader(AuthenticationConstants.Authentication.HTTP_HEADER_AUTHORIZATION))
                .thenReturn(jwt);

        CreateCategoryUseCase.Input input = CreateCategoryUseCase.Input.builder()
                .expenseCategoryCreate(expenseCategoryCreate)
                .jwt(jwt)
                .build();

        CreateCategoryUseCase.Output output = CreateCategoryUseCase.Output.builder()
                .expenseCategory(expectedExpenseCategory)
                .build();

        when(createCategoryUseCase.execute(input)).thenReturn(output);

        // When
        ResponseEntity<ExpenseCategory> result = expenseCategoryController.create(expenseCategoryCreate);

        // Then
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(expectedExpenseCategory, result.getBody());
        verify(createCategoryUseCase, times(1)).execute(any(CreateCategoryUseCase.Input.class));
    }

    @Test
    void testList_shouldReturnExpenseCategoryListAndHttpStatusOk() {
        // Given
        Integer offset = 0;
        Integer limit = 10;
        String ids = "test-ids";

        ExpenseCategory expenseCategory = new ExpenseCategory();
        List<ExpenseCategory> expectedExpenseCategories = new ArrayList<>(List.of(expenseCategory));
        String jwt = "test-jwt";

        when(httpServletRequest.getHeader(AuthenticationConstants.Authentication.HTTP_HEADER_AUTHORIZATION))
                .thenReturn(jwt);

        ListCategoriesUseCase.Input input = ListCategoriesUseCase.Input.builder()
                .offset(offset)
                .limit(limit)
                .ids(List.of(ids.split(",")))
                .jwt(jwt)
                .build();

        ListCategoriesUseCase.Output output = ListCategoriesUseCase.Output.builder()
                .expenseCategories(expectedExpenseCategories)
                .build();

        when(listCategoriesUseCase.execute(input)).thenReturn(output);

        // When
        ResponseEntity<List<ExpenseCategory>> result = expenseCategoryController.list(offset, limit, ids);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedExpenseCategories, result.getBody());
        verify(listCategoriesUseCase, times(1)).execute(any(ListCategoriesUseCase.Input.class));
    }

    @Test
    void testDelete_shouldReturnHttpStatusNoContent() {
        // Given
        String id = "test-categoryId";
        String jwt = "test-jwt";

        when(httpServletRequest.getHeader(AuthenticationConstants.Authentication.HTTP_HEADER_AUTHORIZATION))
                .thenReturn(jwt);

        DeleteCategoryUseCase.Input input = DeleteCategoryUseCase.Input.builder()
                .categoryId(id)
                .jwt(jwt)
                .build();

        // When
        ResponseEntity<Void> result = expenseCategoryController.delete(id);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(deleteCategoryUseCase, times(1)).execute(any(DeleteCategoryUseCase.Input.class));
    }
}
