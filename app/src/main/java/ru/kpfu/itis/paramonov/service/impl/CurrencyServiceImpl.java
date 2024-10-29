package ru.kpfu.itis.paramonov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.kpfu.itis.paramonov.config.CurrencyApiConfigurationProperties;
import ru.kpfu.itis.paramonov.dto.CurrencyApiConvertRequestDto;
import ru.kpfu.itis.paramonov.dto.CurrencyDto;
import ru.kpfu.itis.paramonov.exception.RemoteGatewayErrorException;
import ru.kpfu.itis.paramonov.service.CurrencyService;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final WebClient webClient;

    private final CurrencyApiConfigurationProperties currencyApiConfigurationProperties;

    @Override
    public Mono<CurrencyDto> convertCurrenciesWithMono(String fromCurrency, Double budget) {
        return retrieveResponseBody(fromCurrency, budget)
                .bodyToMono(CurrencyDto.class)
                .onErrorResume(e -> Mono.error(
                        new RemoteGatewayErrorException("Remote gateway error")
                ));
    }

    @Override
    public CompletableFuture<CurrencyDto> convertCurrenciesWithCompletableFuture(String fromCurrency, Double budget) {
        return retrieveResponseBody(fromCurrency, budget)
                .bodyToMono(CurrencyDto.class)
                .onErrorResume(e -> Mono.error(
                        new RemoteGatewayErrorException("Remote gateway error")
                ))
                .toFuture();
    }

    private WebClient.ResponseSpec retrieveResponseBody(String fromCurrency, Double budget) {
        var requestBody = new CurrencyApiConvertRequestDto(fromCurrency, "RUB", budget);
        return webClient.post()
                .uri(currencyApiConfigurationProperties.getConvertCurrencyUri())
                .bodyValue(requestBody)
                .retrieve();
    }
}
