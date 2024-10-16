package usecases.expense;

import com.portfolio.expensetracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.dto.ExpenseUpdate;
import com.portfolio.expensetracker.dto.portfoliomanager.response.AssetResponse;
import com.portfolio.expensetracker.exception.AuthorizationFailedException;
import com.portfolio.expensetracker.security.context.DigitalUser;
import com.portfolio.expensetracker.usecases.expense.FindByIdUseCase;
import com.portfolio.expensetracker.usecases.expense.UpdateUseCase;
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

public class UpdateUseCaseTest {

    @Mock
    private ExpenseDataProvider dataProvider;

    @Mock
    private FindByIdUseCase findByIdUseCase;

    @InjectMocks
    private UpdateUseCase updateUseCase;

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
    void test_execute_shouldUpdateAndReturnExpenseWhenUserIsTheOwner() {
        // Given
        String jwt = "test-jwt";
        String id = "test-id";
        ExpenseUpdate expenseUpdate = new ExpenseUpdate();

        AssetResponse assetResponse = new AssetResponse();
        assetResponse.setPermissionPolicy(AssetResponse.PermissionPolicy.OWNER);
        Expense expectedExpense = new Expense();

        when(findByIdUseCase.execute(any(FindByIdUseCase.Input.class)))
                .thenReturn(new FindByIdUseCase.Output(null, assetResponse));
        when(dataProvider.update(id, expenseUpdate)).thenReturn(expectedExpense);

        UpdateUseCase.Input input = UpdateUseCase.Input.builder()
                .jwt(jwt)
                .id(id)
                .expenseUpdate(expenseUpdate)
                .build();

        // When
        UpdateUseCase.Output result = updateUseCase.execute(input);

        // Then
        assertNotNull(result);
        assertEquals(expectedExpense, result.getExpense());
        verify(findByIdUseCase).execute(any(FindByIdUseCase.Input.class));
        verify(dataProvider).update(id, expenseUpdate);
    }

    @Test
    void test_execute_shouldThrowAuthorizationFailedExceptionWhenUserIsNotTheOwner() {
        // Given
        String jwt = "test-jwt";
        String id = "test-id";
        ExpenseUpdate expenseUpdate = new ExpenseUpdate();

        AssetResponse assetResponse = new AssetResponse();
        assetResponse.setPermissionPolicy(AssetResponse.PermissionPolicy.VIEWER);

        when(findByIdUseCase.execute(any(FindByIdUseCase.Input.class)))
                .thenReturn(new FindByIdUseCase.Output(null, assetResponse));

        UpdateUseCase.Input input = UpdateUseCase.Input.builder()
                .jwt(jwt)
                .id(id)
                .expenseUpdate(expenseUpdate)
                .build();

        // When & Then
        assertThrows(AuthorizationFailedException.class, () -> updateUseCase.execute(input));
        verify(findByIdUseCase).execute(any(FindByIdUseCase.Input.class));
    }
}
