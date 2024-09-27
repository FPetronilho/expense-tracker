package com.portfolio.expensetracker.portfoliomanager.client;

import com.portfolio.expensetracker.portfoliomanager.config.FeignConfig;
import com.portfolio.expensetracker.dto.portfoliomanager.request.AssetRequest;
import com.portfolio.expensetracker.dto.portfoliomanager.response.AssetResponse;
import com.portfolio.expensetracker.util.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

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
            @RequestBody @Valid AssetRequest assetRequest);

    @GetMapping("/assets")
    List<AssetResponse> listAssets(
            @RequestParam(required = false, defaultValue = Constants.DEFAULT_OFFSET)
            @Min(value = Constants.MIN_OFFSET, message = Constants.OFFSET_INVALID_MSG) Integer offset,

            @RequestParam(required = false, defaultValue = Constants.DEFAULT_LIMIT)
            @Min(value = Constants.MIN_LIMIT, message = Constants.LIMIT_INVALID_MSG)
            @Max(value = Constants.MAX_LIMIT, message = Constants.LIMIT_INVALID_MSG) Integer limit,

            @RequestParam(required = false)
            @Pattern(regexp = Constants.ID_LIST_REGEX,
                    message = Constants.IDS_INVALID_MSG) String ids,

            @RequestParam(name = "artifactInfo.groupId")
            @Pattern(regexp = Constants.GROUP_ID_REGEX,
                    message = Constants.GROUP_ID_INVALID_MSG) String groupId,

            @RequestParam(name = "artifactInfo.artifactId")
            @Pattern(regexp = Constants.ARTIFACT_ID_REGEX,
                    message = Constants.ARTIFACT_ID_INVALID_MSG) String artifactId,

            @RequestParam
            @Pattern(regexp = Constants.TYPE_REGEX,
                    message = Constants.TYPE_INVALID_MSG) String type,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAtLte,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAtGte
    );
}
