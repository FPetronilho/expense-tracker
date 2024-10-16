package usecases.expensecategory;

import com.portfolio.expensetracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.portfoliomanager.response.AssetResponse;
import com.portfolio.expensetracker.security.context.DigitalUser;
import com.portfolio.expensetracker.usecases.expensecategory.ListCategoriesUseCase;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ListCategoriesUseCaseTest {

    @Mock
    private ExpenseCategoryDataProvider dataProvider;

    @Mock
    private PortfolioManagerDataProvider portfolioManagerDataProvider;

    @InjectMocks
    private ListCategoriesUseCase listCategoriesUseCase;

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
    void testExecute_shouldReturnExpenseCategoryList() {
        // Given
        String jwt = "test-jwt";
        Integer offset = 0;
        Integer limit = 10;
        List<String> ids = new ArrayList<>(List.of("test-ids"));

        ExpenseCategory expenseCategory = new ExpenseCategory();
        List<ExpenseCategory> expectedExpenseCategories = new ArrayList<>(List.of(expenseCategory));

        AssetResponse assetResponse = new AssetResponse();
        assetResponse.setExternalId("test-ids");
        List<AssetResponse> assetResponseList = new ArrayList<>(List.of(assetResponse));

        when(portfolioManagerDataProvider.listAssets(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(assetResponseList);
        when(dataProvider.list(offset, limit, ids)).thenReturn(expectedExpenseCategories);

        ListCategoriesUseCase.Input input = ListCategoriesUseCase.Input.builder()
                .jwt(jwt)
                .offset(offset)
                .limit(limit)
                .ids(ids)
                .build();

        // When
        ListCategoriesUseCase.Output result = listCategoriesUseCase.execute(input);

        // Then
        assertNotNull(result);
        assertEquals(expectedExpenseCategories, result.getExpenseCategories());
        verify(portfolioManagerDataProvider).listAssets(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        verify(dataProvider).list(offset, limit, ids);
    }
}
