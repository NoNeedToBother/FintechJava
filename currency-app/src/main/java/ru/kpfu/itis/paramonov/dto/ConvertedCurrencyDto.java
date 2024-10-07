package ru.kpfu.itis.paramonov.dto;

public record ConvertedCurrencyDto(
        String fromCurrency, String toCurrency, Double convertedAmount
) {
}
