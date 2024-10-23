package ru.kpfu.itis.paramonov.dto;

public record CurrencyDto(
        String fromCurrency,
        String toCurrency,
        Double convertedAmount
) {}
