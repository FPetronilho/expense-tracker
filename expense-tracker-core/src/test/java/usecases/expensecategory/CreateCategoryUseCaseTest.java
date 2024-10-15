package usecases.expensecategory;

import com.portfolio.expensetracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCategoryCreate;
import com.portfolio.expensetracker.dto.portfoliomanager.request.AssetRequest;
import com.portfolio.expensetracker.exception.ResourceAlreadyExistsException;
import com.portfolio.expensetracker.security.context.DigitalUser;
import com.portfolio.expensetracker.usecases.expensecategory.CreateCategoryUseCase;
import com.portfolio.expensetracker.usecases.expensecategory.ListCategoriesUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateCategoryUseCaseTest {

    @Mock
    private ExpenseCategoryDataProvider dataProvider;

    @Mock
    private PortfolioManagerDataProvider portfolioManagerDataProvider;

    @Mock
    private ListCategoriesUseCase listCategoriesUseCase;

    @InjectMocks
    private CreateCategoryUseCase createCategoryUseCase;

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
    void testExecute_shouldReturnExpenseCategoryWhenEverythingIsValid() {
        // Given
        String jwt = "test-jwt";
        ExpenseCategoryCreate expenseCategoryCreate = new ExpenseCategoryCreate();

        String digitalUserId = "test-digitalUserId";
        ExpenseCategory expectedExpenseCategory = new ExpenseCategory();

        CreateCategoryUseCase.Input input = CreateCategoryUseCase.Input.builder()
                .jwt(jwt)
                .expenseCategoryCreate(expenseCategoryCreate)
                .build();

        when(listCategoriesUseCase.execute(any())).thenReturn(new  ListCategoriesUseCase.Output(List.of()));
        when(dataProvider.create(expenseCategoryCreate)).thenReturn(expectedExpenseCategory);

        // When
        CreateCategoryUseCase.Output result = createCategoryUseCase.execute(input);

        // Then
        assertNotNull(result);
        assertEquals(expectedExpenseCategory, result.getExpenseCategory());
        verify(dataProvider).create(expenseCategoryCreate);
        verify(listCategoriesUseCase).execute(any(ListCategoriesUseCase.Input.class));
        verify(portfolioManagerDataProvider).createAsset(eq(jwt), eq(digitalUserId), any(AssetRequest.class));
    }

    @Test
    void testExecute_shouldThrowResourceAlreadyExistsExceptionWhenExpenseCategoryAlreadyExists() {
        // Given
        String jwt = "test-jwt";
        ExpenseCategoryCreate expenseCategoryCreate = new ExpenseCategoryCreate();
        expenseCategoryCreate.setName("test-name");

        ExpenseCategory existingExpenseCategory = new ExpenseCategory();
        existingExpenseCategory.setName("test-name");

        when(listCategoriesUseCase.execute(any()))
                .thenReturn(new ListCategoriesUseCase.Output(List.of(existingExpenseCategory)));

        CreateCategoryUseCase.Input input = CreateCategoryUseCase.Input.builder()
                .jwt(jwt)
                .expenseCategoryCreate(expenseCategoryCreate)
                .build();

        // When & Then
        assertThrows(ResourceAlreadyExistsException.class, () -> createCategoryUseCase.execute(input));
    }

    @Test
    void testExecute_shouldThrowExceptionWhenPortfolioManagerCallIsNotSuccessful() {
        // Given
        String jwt = "test-jwt";
        ExpenseCategoryCreate expenseCategoryCreate = new ExpenseCategoryCreate();

        String digitalUserId = "test-digitalUserId";
        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setId("test-id");

        when(listCategoriesUseCase.execute(any())).thenReturn(new ListCategoriesUseCase.Output(List.of()));
        when(dataProvider.create(expenseCategoryCreate)).thenReturn(expenseCategory);
        doThrow(new RuntimeException("Portfolio Manager failed."))
                .when(portfolioManagerDataProvider).createAsset(eq(jwt), eq(digitalUserId), any(AssetRequest.class));

        CreateCategoryUseCase.Input input = CreateCategoryUseCase.Input.builder()
                .jwt(jwt)
                .expenseCategoryCreate(expenseCategoryCreate)
                .build();

        // When & Then
        assertThrows(RuntimeException.class, () -> createCategoryUseCase.execute(input));
        verify(dataProvider).delete(expenseCategory.getId());
    }
}
