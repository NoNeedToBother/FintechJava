package ru.kpfu.itis.paramonov.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.kpfu.itis.paramonov.dto.api.CurrenciesApiResponseDto;
import ru.kpfu.itis.paramonov.service.CurrenciesApiService;

@Service
public class CurrenciesApiServiceImpl implements CurrenciesApiService {

    @Autowired
    @Qualifier("cb_client")
    private RestClient restClient;

    @Value("${api.cbr.currency.uri}")
    private String currencyRateEndpoint;

    @Override
    @Cacheable("currency_rates")
    public CurrenciesApiResponseDto getAllCurrencies() {
        return restClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path(currencyRateEndpoint).build())
                .retrieve()
                .toEntity(CurrenciesApiResponseDto.class)
                .getBody();
    }
}
