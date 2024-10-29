package ru.kpfu.itis.paramonov.service;

import reactor.core.publisher.Mono;
import ru.kpfu.itis.paramonov.dto.CurrencyDto;

import java.util.concurrent.CompletableFuture;

public interface CurrencyService {

    Mono<CurrencyDto> convertCurrenciesWithMono(String fromCurrency, Double budget);

    CompletableFuture<CurrencyDto> convertCurrenciesWithCompletableFuture(String fromCurrency, Double budget);
}
