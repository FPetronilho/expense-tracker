package com.portfolio.expensetracker.dataprovider;

import com.portfolio.expensetracker.dto.portfoliomanager.request.AssetRequest;
import com.portfolio.expensetracker.dto.portfoliomanager.response.AssetResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface PortfolioManagerDataProvider {

    AssetResponse createAsset(
            String jwt,
            String digitalUserId,
            AssetRequest assetRequest
    );

    List<AssetResponse> listAssets(
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
    );

    void deleteAsset(
            String jwt,
            String externalId
    );
}