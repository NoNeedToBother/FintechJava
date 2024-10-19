package ru.kpfu.itis.paramonov.service.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.kpfu.itis.paramonov.config.CentralBankApiConfigurationProperties;
import ru.kpfu.itis.paramonov.dto.api.CurrenciesApiResponseDto;
import ru.kpfu.itis.paramonov.exception.CurrencyApiGatewayErrorException;
import ru.kpfu.itis.paramonov.service.CurrenciesApiService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class CurrenciesApiServiceImpl implements CurrenciesApiService {

    @Autowired
    @Qualifier("central_bank_api_client")
    private RestClient restClient;

    @Autowired
    private CentralBankApiConfigurationProperties centralBankApiConfigurationProperties;

    @Override
    @Cacheable("currency_rates")
    @CircuitBreaker(name = "currencies", fallbackMethod = "currenciesFallback")
    public CurrenciesApiResponseDto getAllCurrencies() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate current = LocalDate.now();
        try {
            return restClient.get()
                    .uri(uriBuilder ->
                            uriBuilder.path(centralBankApiConfigurationProperties.getCurrencyUri())
                                    .queryParam("date_req", current.format(dateFormatter))
                                    .build())
                    .retrieve()
                    .toEntity(CurrenciesApiResponseDto.class)
                    .getBody();
        } catch (Exception e) {
            throw new CurrencyApiGatewayErrorException("Failed to get data from external API", e);
        }
    }

    public String currenciesFallback(Exception e) {
        log.warn("Circuit breaker opened since currency endpoint failed to answer with exception: ", e);
        return "";
    }
}
