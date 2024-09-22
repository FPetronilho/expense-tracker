package com.portfolio.expensetracker.portfoliomanager.client;

import com.portfolio.expensetracker.portfoliomanager.config.FeignConfig;
import com.portfolio.expensetracker.portfoliomanager.dto.Asset;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "portfolio-management-http-client",
        url = "${http.url.portfolio-manager}", // microservice base path
        configuration = FeignConfig.class
)
public interface PortfolioManagerHttpClient {

    @GetMapping("/assets")
    Asset list(
            @RequestParam int offset,
            @RequestParam int limit
    );

}
