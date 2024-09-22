package com.portfolio.expensetracker.portfoliomanager.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean("http2PatchEnabler")
    public OkHttpClient client() {
        // Custom HTTP client that supports HTTP/2 and PATCH methods
        return new OkHttpClient();
    }

}
