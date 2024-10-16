package usecases.expense;

import com.portfolio.expensetracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCreate;
import com.portfolio.expensetracker.dto.portfoliomanager.request.AssetRequest;
import com.portfolio.expensetracker.security.context.DigitalUser;
import com.portfolio.expensetracker.usecases.expense.CreateUseCase;
import com.portfolio.expensetracker.usecases.expensecategory.FindCategoryByIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateUseCaseTest {

    @Mock
    private ExpenseDataProvider dataProvider;

    @Mock
    private FindCategoryByIdUseCase findCategoryByIdUseCase;

    @Mock
    private PortfolioManagerDataProvider portfolioManagerDataProvider;

    @InjectMocks
    private CreateUseCase createUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Mock SecurityContext and Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        // Mock DigitalUser
        DigitalUser digitalUser = new DigitalUser("test-digitalUserId", "test-subject");

        // Configure the behavior of mocks
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(digitalUser);

        // Set the SecurityContext in SecurityContextHolder
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testExecute_shouldReturnExpenseWhenPortfolioManagerCallIsSuccessful() {
        // Given
        String jwt = "test-jwt";
        ExpenseCreate expenseCreate = new ExpenseCreate();

        ExpenseCategory expenseCategory = new ExpenseCategory();
        Expense expectedExpense = new Expense();
        String digitalUserId = "test-digitalUserId";

        when(findCategoryByIdUseCase.execute(any(FindCategoryByIdUseCase.Input.class)))
                .thenReturn(new FindCategoryByIdUseCase.Output(expenseCategory, null));
        when(dataProvider.create(expenseCreate, expenseCategory)).thenReturn(expectedExpense);

        CreateUseCase.Input input = CreateUseCase.Input.builder()
                .jwt(jwt)
                .expenseCreate(expenseCreate)
                .build();

        // When
        CreateUseCase.Output result = createUseCase.execute(input);

        // Then
        assertNotNull(result);
        assertEquals(expectedExpense, result.getExpense());
        verify(dataProvider).create(expenseCreate, expenseCategory);
        verify(portfolioManagerDataProvider).createAsset(eq(jwt), eq(digitalUserId), any(AssetRequest.class));
    }

    @Test
    void testExecute_shouldThrowExceptionWhenPortfolioManagerCallIsNotSuccessful() {
        // Given
        String jwt = "test-jwt";
        ExpenseCreate expenseCreate = new ExpenseCreate();

        ExpenseCategory expenseCategory = new ExpenseCategory();
        Expense expense = new Expense();
        String digitalUserId = "test-digitalUserId";

        when(findCategoryByIdUseCase.execute(any(FindCategoryByIdUseCase.Input.class)))
                .thenReturn(new FindCategoryByIdUseCase.Output(expenseCategory, null));
        when(dataProvider.create(expenseCreate, expenseCategory)).thenReturn(expense);

        CreateUseCase.Input input = CreateUseCase.Input.builder()
                .jwt(jwt)
                .expenseCreate(expenseCreate)
                .build();

        doThrow(new  RuntimeException("Portfolio Manager failed."))
                .when(portfolioManagerDataProvider).createAsset(eq(jwt), eq(digitalUserId), any(AssetRequest.class));

        // When & Then
        assertThrows(RuntimeException.class, () -> createUseCase.execute(input));
        verify(dataProvider).delete(expense.getId());
    }
}
