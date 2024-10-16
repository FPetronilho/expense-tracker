package usecases.expensecategory;

import com.portfolio.expensetracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.portfoliomanager.response.AssetResponse;
import com.portfolio.expensetracker.exception.ResourceNotFoundException;
import com.portfolio.expensetracker.security.context.DigitalUser;
import com.portfolio.expensetracker.usecases.expensecategory.FindCategoryByIdUseCase;
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

public class FindCategoryByIdUseCaseTest {

    @Mock
    private ExpenseCategoryDataProvider dataProvider;

    @Mock
    private PortfolioManagerDataProvider portfolioManagerDataProvider;

    @InjectMocks
    private FindCategoryByIdUseCase findCategoryByIdUseCase;

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
    void testExecute_shouldReturnExpenseCategoryAndAssetResponseWhenExpenseCategoryExists() {
        // Given
        String jwt = "test-jwt";
        String id = "test-id";

        ExpenseCategory expectedExpenseCategory = new ExpenseCategory();
        AssetResponse expectedAssetResponse = new AssetResponse();
        List<AssetResponse> assetResponseList = new ArrayList<>(List.of(expectedAssetResponse));

        when(portfolioManagerDataProvider.listAssets(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(assetResponseList);
        when(dataProvider.findById(id)).thenReturn(expectedExpenseCategory);

        FindCategoryByIdUseCase.Input input = FindCategoryByIdUseCase.Input.builder()
                .jwt(jwt)
                .id(id)
                .build();

        // When
        FindCategoryByIdUseCase.Output result = findCategoryByIdUseCase.execute(input);

        // Then
        assertNotNull(result);
        assertEquals(expectedAssetResponse, result.getAsset());
        assertEquals(expectedExpenseCategory, result.getExpenseCategory());
        verify(dataProvider).findById(id);
        verify(portfolioManagerDataProvider).listAssets(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void testExecute_shouldThrowResourceNotFoundExceptionWhenExpenseCategoryExist() {
        // Given
        String jwt = "test-jwt";
        String id = "test-id";

        when(portfolioManagerDataProvider.listAssets(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(null);

        FindCategoryByIdUseCase.Input input = FindCategoryByIdUseCase.Input.builder()
                .jwt(jwt)
                .id(id)
                .build();

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> findCategoryByIdUseCase.execute(input));
        verify(portfolioManagerDataProvider).listAssets(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
    }
}
