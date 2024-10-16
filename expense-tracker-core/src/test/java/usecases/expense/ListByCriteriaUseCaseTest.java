package usecases.expense;

import com.portfolio.expensetracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.domain.OrderBy;
import com.portfolio.expensetracker.domain.OrderDirection;
import com.portfolio.expensetracker.dto.portfoliomanager.response.AssetResponse;
import com.portfolio.expensetracker.security.context.DigitalUser;
import com.portfolio.expensetracker.usecases.expense.ListByCriteriaUseCase;
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

public class ListByCriteriaUseCaseTest {

    @Mock
    private ExpenseDataProvider dataProvider;

    @Mock
    private PortfolioManagerDataProvider portfolioManagerDataProvider;

    @InjectMocks
    private ListByCriteriaUseCase listByCriteriaUseCase;

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
    void testExecute_shouldReturnExpenseList() {
        // Given
        String jwt = "test-jwt";
        Integer offset = 0;
        Integer limit = 10;
        String categoryId = "test-categoryId";
        List<OrderBy> orderByList = new ArrayList<>(List.of(OrderBy.DATE));
        List<OrderDirection> orderDirectionList = new ArrayList<>(List.of(OrderDirection.ASC));
        List<String> ids = new ArrayList<>(List.of("test-ids"));

        AssetResponse assetResponse = new AssetResponse();
        assetResponse.setExternalId("test-ids");
        List<AssetResponse> assetResponseList = new ArrayList<>(List.of(assetResponse));
        Expense expense = new Expense();
        List<Expense> expectedExpenses = new ArrayList<>(List.of(expense));

        when(portfolioManagerDataProvider.listAssets(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(assetResponseList);

        ListByCriteriaUseCase.Input input = ListByCriteriaUseCase.Input.builder()
                .jwt(jwt)
                .offset(offset)
                .limit(limit)
                .categoryId(categoryId)
                .to(null)
                .from(null)
                .date(null)
                .amount(null)
                .amountLte(null)
                .amountGte(null)
                .orderByList(orderByList)
                .orderDirectionList(orderDirectionList)
                .ids(ids)
                .build();

        when(dataProvider.listByCriteria(input)).thenReturn(expectedExpenses);

        // When
        ListByCriteriaUseCase.Output results = listByCriteriaUseCase.execute(input);

        // Then
        assertNotNull(results);
        assertEquals(expectedExpenses, results.getExpenses());
        verify(portfolioManagerDataProvider).listAssets(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        verify(dataProvider).listByCriteria(input);
    }
}
