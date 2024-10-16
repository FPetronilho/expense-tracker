package usecases.expensecategory;

import com.portfolio.expensetracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.dto.portfoliomanager.response.AssetResponse;
import com.portfolio.expensetracker.exception.AuthorizationFailedException;
import com.portfolio.expensetracker.security.context.DigitalUser;
import com.portfolio.expensetracker.usecases.expensecategory.DeleteCategoryUseCase;
import com.portfolio.expensetracker.usecases.expensecategory.FindCategoryByIdUseCase;
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

public class DeleteCategoryUseCaseTest {

    @Mock
    private FindCategoryByIdUseCase findCategoryByIdUseCase;

    @Mock
    private ExpenseCategoryDataProvider dataProvider;

    @Mock
    private PortfolioManagerDataProvider portfolioManagerDataProvider;

    @InjectMocks
    private DeleteCategoryUseCase deleteCategoryUseCase;

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
    void testExecute_shouldDeleteExpenseCategoryIfUserIsTheOwner() {
        // Given
        String jwt = "test-jwt";
        String categoryId = "test-categoryId";

        AssetResponse assetResponse = new AssetResponse();
        assetResponse.setPermissionPolicy(AssetResponse.PermissionPolicy.OWNER);
        String digitalUserId = "test-digitalUserId";
        when(findCategoryByIdUseCase.execute(any(FindCategoryByIdUseCase.Input.class)))
                .thenReturn(new FindCategoryByIdUseCase.Output(any(), assetResponse));

        DeleteCategoryUseCase.Input input = DeleteCategoryUseCase.Input.builder()
                .jwt(jwt)
                .categoryId(categoryId)
                .build();

        // When
        deleteCategoryUseCase.execute(input);

        // Then
        verify(dataProvider).delete(categoryId);
        verify(portfolioManagerDataProvider).deleteAsset(jwt, digitalUserId, categoryId);
    }

    @Test
    void testExecute_shouldThrowAuthorizationFailedExceptionIfUserIsNotTheOwner() {
        // Given
        String jwt = "test-jwt";
        String categoryId = "test-categoryId";

        AssetResponse assetResponse = new AssetResponse();
        assetResponse.setPermissionPolicy(AssetResponse.PermissionPolicy.VIEWER);
        when(findCategoryByIdUseCase.execute(any(FindCategoryByIdUseCase.Input.class)))
                .thenReturn(new  FindCategoryByIdUseCase.Output(any(), assetResponse));

        DeleteCategoryUseCase.Input input = DeleteCategoryUseCase.Input.builder()
                .jwt(jwt)
                .categoryId(categoryId)
                .build();

        // When & Then
        assertThrows(AuthorizationFailedException.class, () -> deleteCategoryUseCase.execute(input));
    }
}
