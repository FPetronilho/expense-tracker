package controller;

import com.portfolio.expensetracker.controller.ExpenseController;
import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.domain.OrderBy;
import com.portfolio.expensetracker.domain.OrderDirection;
import com.portfolio.expensetracker.dto.ExpenseCreate;
import com.portfolio.expensetracker.dto.ExpenseUpdate;
import com.portfolio.expensetracker.dto.portfoliomanager.response.AssetResponse;
import com.portfolio.expensetracker.usecases.expense.*;
import com.portfolio.expensetracker.util.AuthenticationConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExpenseControllerTest {

    @Mock
    private CreateUseCase createUseCase;

    @Mock
    private FindByIdUseCase findByIdUseCase;

    @Mock
    private ListByCriteriaUseCase listByCriteriaUseCase;

    @Mock
    private UpdateUseCase updateUseCase;

    @Mock
    private DeleteUseCase deleteUseCase;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private ExpenseController expenseController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_shouldReturnExpenseAndHttpStatusCreated() {
        // Given
        ExpenseCreate expenseCreate = new ExpenseCreate();

        Expense expectedExpense = new Expense();
        String jwt = "test-jwt";

        when(httpServletRequest.getHeader(AuthenticationConstants.Authentication.HTTP_HEADER_AUTHORIZATION))
                .thenReturn(jwt);

        CreateUseCase.Input input = CreateUseCase.Input.builder()
                .expenseCreate(expenseCreate)
                .jwt(jwt)
                .build();

        CreateUseCase.Output output = CreateUseCase.Output.builder()
                .expense(expectedExpense)
                .build();

        when(createUseCase.execute(input)).thenReturn(output);

        // When
        ResponseEntity<Expense> result = expenseController.create(expenseCreate);

        // Then
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(expectedExpense, result.getBody());
        verify(createUseCase, times(1)).execute(any(CreateUseCase.Input.class));
    }

    @Test
    void testFindById_shouldReturnExpenseAndHttpStatusOk() {
        // Given
        String id = "test-id";

        Expense expectedExpense = new Expense();
        AssetResponse expectedAssetResponse = new AssetResponse();
        String jwt = "test-jwt";

        when(httpServletRequest.getHeader(AuthenticationConstants.Authentication.HTTP_HEADER_AUTHORIZATION))
                .thenReturn(jwt);

        FindByIdUseCase.Input input = FindByIdUseCase.Input.builder()
                .id(id)
                .jwt(jwt)
                .build();

        FindByIdUseCase.Output output = FindByIdUseCase.Output.builder()
                .expense(expectedExpense)
                .asset(expectedAssetResponse)
                .build();

        when(findByIdUseCase.execute(input)).thenReturn(output);

        // When
        ResponseEntity<Expense> result = expenseController.findById(id);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedExpense, result.getBody());
        assertEquals(expectedAssetResponse, output.getAsset());
        verify(findByIdUseCase, times(1)).execute(any(FindByIdUseCase.Input.class));
    }

    @Test
    void testListByCriteria_shouldReturnExpenseListAndHttpStatusOk() {
        // Given
        Integer offset = 0;
        Integer limit = 10;
        String categoryId = "test-categoryId";
        List<OrderBy> orderByList = List.of(OrderBy.AMOUNT);
        List<OrderDirection> orderDirectionList = List.of(OrderDirection.ASC);
        String ids = "test-ids";

        Expense expense = new Expense();
        List<Expense> expectedExpenses = List.of(expense);
        String jwt = "test-jwt";

        when(httpServletRequest.getHeader(AuthenticationConstants.Authentication.HTTP_HEADER_AUTHORIZATION))
                .thenReturn(jwt);

        ListByCriteriaUseCase.Input input = ListByCriteriaUseCase.Input.builder()
                .offset(offset)
                .limit(limit)
                .categoryId(categoryId)
                .to(null)
                .date(null)
                .from(null)
                .amountLte(null)
                .amount(null)
                .amountGte(null)
                .orderByList(orderByList)
                .orderDirectionList(orderDirectionList)
                .ids(List.of(ids.split(",")))
                .jwt(jwt)
                .build();

        ListByCriteriaUseCase.Output output = ListByCriteriaUseCase.Output.builder()
                .expenses(expectedExpenses)
                .build();

        when(listByCriteriaUseCase.execute(input)).thenReturn(output);

        // When
        ResponseEntity<List<Expense>> result = expenseController.listByCriteria(
                offset,
                limit,
                categoryId,
                null,
                null,
                null,
                null,
                null,
                null,
                orderByList,
                orderDirectionList,
                ids
        );

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedExpenses, result.getBody());
        verify(listByCriteriaUseCase, times(1)).execute(any(ListByCriteriaUseCase.Input.class));
    }

    @Test
    void testUpdate_shouldReturnExpenseAndHttpStatusOk() {
        // Given
        String id = "test-id";
        ExpenseUpdate expenseUpdate = new ExpenseUpdate();

        Expense expectedExpense = new Expense();
        String jwt = "test-jwt";

        when(httpServletRequest.getHeader(AuthenticationConstants.Authentication.HTTP_HEADER_AUTHORIZATION))
                .thenReturn(jwt);

        UpdateUseCase.Input input = UpdateUseCase.Input.builder()
                .id(id)
                .expenseUpdate(expenseUpdate)
                .jwt(jwt)
                .build();

        UpdateUseCase.Output output = UpdateUseCase.Output.builder()
                .expense(expectedExpense)
                .build();

        when(updateUseCase.execute(input)).thenReturn(output);

        // When
        ResponseEntity<Expense> result = expenseController.update(id, expenseUpdate);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedExpense, result.getBody());
        verify(updateUseCase, times(1)).execute(any(UpdateUseCase.Input.class));
    }

    @Test
    void testDelete_shouldReturnHttpStatusNoContent() {
        // Given
        String id = "test-id";
        String jwt = "test-jwt";

        when(httpServletRequest.getHeader(AuthenticationConstants.Authentication.HTTP_HEADER_AUTHORIZATION))
                .thenReturn(jwt);

        DeleteUseCase.Input input = DeleteUseCase.Input.builder()
                .id(id)
                .jwt(jwt)
                .build();

        // When
        ResponseEntity<Void> result = expenseController.delete(id);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(deleteUseCase, times(1)).execute(any(DeleteUseCase.Input.class));
    }
}
