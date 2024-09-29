package com.portfolio.expensetracker.portfoliomanager.client;

import com.portfolio.expensetracker.dto.portfoliomanager.request.AssetRequest;
import com.portfolio.expensetracker.dto.portfoliomanager.response.AssetResponse;
import com.portfolio.expensetracker.portfoliomanager.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(
        name = "portfolio-management-http-client",
        url = "${http.url.portfolio-manager}", // microservice base path
        configuration = FeignConfig.class
)
public interface PortfolioManagerHttpClient {

    @PostMapping("/assets/{digitalUserId}")
    AssetResponse createAsset(
            @RequestHeader("Authorization") String authorization,
            @PathVariable String digitalUserId,
            @RequestBody AssetRequest assetRequest
    );

    @GetMapping("/assets")
    List<AssetResponse> listAssets(
            @RequestHeader("Authorization") String authorization,
            @RequestParam String digitalUserId,
            @RequestParam Integer offset,
            @RequestParam Integer limit,
            @RequestParam("artifactInfo.groupId") String groupId,
            @RequestParam("artifactInfo.artifactId") String artifactId,
            @RequestParam String type,
            @RequestParam LocalDateTime createdAtLte,
            @RequestParam LocalDateTime createdAt,
            @RequestParam LocalDateTime createdAtGte
    );
}
