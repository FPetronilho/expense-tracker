package com.portfolio.expensetracker.dataprovider;

import com.portfolio.expensetracker.dto.portfoliomanager.request.AssetRequest;
import com.portfolio.expensetracker.dto.portfoliomanager.response.AssetResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface PortfolioManagerDataProvider {

    AssetResponse createAsset(AssetRequest assetRequest);

    List<AssetResponse> listAssets(
            Integer offset,
            Integer limit,
            String ids,
            String groupId,
            String artifactId,
            String type,
            LocalDateTime createdAtLte,
            LocalDateTime createdAt,
            LocalDateTime createdAtGte
    );
}