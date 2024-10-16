package dataprovider;

import com.portfolio.expensetracker.dto.portfoliomanager.request.AssetRequest;
import com.portfolio.expensetracker.dto.portfoliomanager.response.AssetResponse;
import com.portfolio.expensetracker.portfoliomanager.client.PortfolioManagerHttpClient;
import com.portfolio.expensetracker.portfoliomanager.dataprovider.PortfolioManagerDataProviderRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PortfolioManagerDataProviderRestTest {

    @Mock
    private PortfolioManagerHttpClient client;

    @InjectMocks
    private PortfolioManagerDataProviderRest dataProvider;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAsset_shouldReturnAssetResponse() {
        // Given
        String jwt = "test-jwt";
        String digitalUserId = "test-digitalUserId";
        AssetRequest assetRequest = new AssetRequest();

        AssetResponse expectedAssetResponse = new AssetResponse();
        when(client.createAsset(jwt, digitalUserId, assetRequest)).thenReturn(expectedAssetResponse);

        // When
        AssetResponse result = dataProvider.createAsset(jwt, digitalUserId, assetRequest);

        // Then
        assertNotNull(result);
        assertEquals(expectedAssetResponse, result);
        verify(client).createAsset(jwt, digitalUserId, assetRequest);
    }

    @Test
    void testListAssets_shouldReturnAssetResponseList() {
        // Given
        String jwt = "test-jwt";
        String digitalUserId = "test-digitalUserId";
        List<String> externalIds = new ArrayList<>(List.of());
        Integer offset = 0;
        Integer limit = 10;
        String groupId = "test-groupId";
        String artifactId = "test-artifactId";
        String type = "test-type";

        AssetResponse assetResponse = new AssetResponse();
        List<AssetResponse> expectedAssetResponseList = new ArrayList<>(List.of(assetResponse));

        when(client.listAssets(jwt, digitalUserId, null, offset, limit, groupId, artifactId, type,
                null, null, null))
                .thenReturn(expectedAssetResponseList);

        // When
        List<AssetResponse> results = dataProvider.listAssets(jwt, digitalUserId, externalIds, offset, limit, groupId,
                artifactId, type, null, null, null);

        // Then
        assertNotNull(results);
        assertEquals(expectedAssetResponseList, results);
        verify(client).listAssets(jwt, digitalUserId, null, offset, limit, groupId, artifactId, type,
                null, null, null);
    }

    @Test
    void testDeleteAsset() {
        // Given
        String jwt = "test-jwt";
        String digitalUserId = "test-digitalUserId";
        String externalId = "test-externalId";

        // When
        dataProvider.deleteAsset(jwt, digitalUserId, externalId);

        // Then
        verify(client).deleteAsset(jwt, digitalUserId, externalId);
    }
}
