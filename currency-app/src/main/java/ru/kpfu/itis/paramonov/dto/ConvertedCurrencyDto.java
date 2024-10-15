package ru.kpfu.itis.paramonov.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ConvertedCurrencyDto(
        @Schema(example = "USD")
        String fromCurrency,
        @Schema(example = "EUR")
        String toCurrency,
        @Schema(example = "123.4")
        Double convertedAmount
) {
}
