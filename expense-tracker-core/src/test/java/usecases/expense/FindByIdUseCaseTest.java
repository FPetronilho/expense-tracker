package usecases.expense;

import com.portfolio.expensetracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.dto.portfoliomanager.response.AssetResponse;
import com.portfolio.expensetracker.exception.ResourceNotFoundException;
import com.portfolio.expensetracker.security.context.DigitalUser;
import com.portfolio.expensetracker.usecases.expense.FindByIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FindByIdUseCaseTest {

    @Mock
    private ExpenseDataProvider dataProvider;

    @Mock
    private PortfolioManagerDataProvider portfolioManagerDataProvider;

    @InjectMocks
    private FindByIdUseCase findByIdUseCase;

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
    void testExecute_shouldReturnExpenseAndAssetResponseWhenExpenseExists() {
        // Given
        String jwt = "test-jwt";
        String id = "test-id";

        Expense expectedExpense = new Expense();
        AssetResponse assetResponse = new AssetResponse();
        List<AssetResponse> assetResponseList = new ArrayList<>(List.of(assetResponse));

        when(portfolioManagerDataProvider.listAssets(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(assetResponseList);
        when(dataProvider.findById(id)).thenReturn(expectedExpense);

        FindByIdUseCase.Input input = FindByIdUseCase.Input.builder()
                .jwt(jwt)
                .id(id)
                .build();

        // When
        FindByIdUseCase.Output result = findByIdUseCase.execute(input);

        // Then
        assertNotNull(result);
        assertEquals(expectedExpense, result.getExpense());
        verify(portfolioManagerDataProvider).listAssets(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        verify(dataProvider).findById(id);
    }

    @Test
    void testExecute_shouldThrowResourceNotFoundExceptionWhenExpenseDoesNotExists() {
        // Given
        String jwt = "test-jwt";
        String id = "test-id";

        when(portfolioManagerDataProvider.listAssets(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(null);

        FindByIdUseCase.Input input = FindByIdUseCase.Input.builder()
                .jwt(jwt)
                .id(id)
                .build();

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> findByIdUseCase.execute(input));
        verify(portfolioManagerDataProvider).listAssets(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
    }
}
