package com.portfolio.expensetracker.portfoliomanager.dataprovider;

import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.dto.portfoliomanager.request.AssetRequest;
import com.portfolio.expensetracker.dto.portfoliomanager.response.AssetResponse;
import com.portfolio.expensetracker.portfoliomanager.client.PortfolioManagerHttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortfolioManagerDataProviderRest implements PortfolioManagerDataProvider {

    private final PortfolioManagerHttpClient client;

    @Override
    public AssetResponse createAsset(
            String jwt,
            String digitalUserId,
            AssetRequest assetRequest
    ) {
        return client.createAsset(jwt, digitalUserId, assetRequest);
    }

    @Override
    public List<AssetResponse> listAssets(
            String jwt,
            String digitalUserId,
            List<String> externalIds,
            Integer offset,
            Integer limit,
            String groupId,
            String artifactId,
            String type,
            LocalDateTime createdAtLte,
            LocalDateTime createdAt,
            LocalDateTime createdAtGte
    ) {
        return client.listAssets(
                jwt,
                digitalUserId,
                !CollectionUtils.isEmpty(externalIds)
                        ? String.join(",", externalIds)
                        : null,
                offset,
                limit,
                groupId,
                artifactId,
                type,
                createdAtLte,
                createdAt,
                createdAtGte
        );
    }

    @Override
    public void deleteAsset(
            String jwt,
            String externalId
    ) {
        client.deleteAsset(jwt, externalId);
    }
}