package ru.kpfu.itis.paramonov.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CurrencyRateDto(
        @Schema(example = "USD")
        String currency,
        @Schema(example = "100.1")
        Double rate) {}
