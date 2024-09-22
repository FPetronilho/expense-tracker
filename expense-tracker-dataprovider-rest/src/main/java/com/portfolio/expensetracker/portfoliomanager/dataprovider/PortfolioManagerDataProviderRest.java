package com.portfolio.expensetracker.portfoliomanager.dataprovider;

import com.portfolio.expensetracker.dataprovider.PortfolioManagerDataProvider;
import com.portfolio.expensetracker.portfoliomanager.client.PortfolioManagerHttpClient;
import com.portfolio.expensetracker.portfoliomanager.dto.Asset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortfolioManagerDataProviderRest implements PortfolioManagerDataProvider {

    private final PortfolioManagerHttpClient client;

    @Override
    public void listAssets() {
        Asset asset = client.list(0, 10);
        log.info(String.valueOf(asset));
    }

}