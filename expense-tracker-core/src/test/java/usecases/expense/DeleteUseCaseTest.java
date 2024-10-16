package usecases.expense;

import com.portfolio.expensetracker.dataprovider.ExpenseDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.dto.portfoliomanager.response.AssetResponse;
import com.portfolio.expensetracker.exception.AuthorizationFailedException;
import com.portfolio.expensetracker.security.context.DigitalUser;
import com.portfolio.expensetracker.usecases.expense.DeleteUseCase;
import com.portfolio.expensetracker.usecases.expense.FindByIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DeleteUseCaseTest {

    @Mock
    private ExpenseDataProvider dataProvider;

    @Mock
    private FindByIdUseCase findByIdUseCase;

    @Mock
    private PortfolioManagerDataProvider portfolioManagerDataProvider;

    @InjectMocks
    private DeleteUseCase deleteUseCase;

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
    void testExecute_shouldDeleteExpenseWhenUserIsTheOwner() {
        // Given
        String jwt = "test-jwt";
        String id = "test-id";

        AssetResponse assetResponse = new AssetResponse();
        assetResponse.setPermissionPolicy(AssetResponse.PermissionPolicy.OWNER);
        String digitalUserId = "test-digitalUserId";

        when(findByIdUseCase.execute(any(FindByIdUseCase.Input.class)))
                .thenReturn(new FindByIdUseCase.Output(null, assetResponse));

        DeleteUseCase.Input input = DeleteUseCase.Input.builder()
                .jwt(jwt)
                .id(id)
                .build();

        // When
        deleteUseCase.execute(input);

        // Then
        verify(dataProvider).delete(id);
        verify(portfolioManagerDataProvider).deleteAsset(jwt, digitalUserId, id);
    }

    @Test
    void testExecute_shouldThrowAuthorizationFailedExceptionWhenUserIsNotTheOwner() {
        // Given
        String jwt = "test-jwt";
        String id = "test-id";

        AssetResponse assetResponse = new AssetResponse();
        assetResponse.setPermissionPolicy(AssetResponse.PermissionPolicy.VIEWER);
        String digitalUserId = "test-digitalUserId";

        when(findByIdUseCase.execute(any(FindByIdUseCase.Input.class)))
                .thenReturn(new FindByIdUseCase.Output(null, assetResponse));

        DeleteUseCase.Input input = DeleteUseCase.Input.builder()
                .jwt(jwt)
                .id(id)
                .build();

        // When & Then
        assertThrows(AuthorizationFailedException.class, () -> deleteUseCase.execute(input));
    }
}
