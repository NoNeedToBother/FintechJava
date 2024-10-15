package ru.kpfu.itis.paramonov.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import ru.kpfu.itis.paramonov.validation.ValidCurrency;

@Getter
public class ConvertCurrencyRequestDto {

    @NotNull(message = "Missing \"fromCurrency\" parameter in request body") @ValidCurrency
    @Schema(example = "USD")
    private String fromCurrency;

    @NotNull(message = "Missing \"toCurrency\" parameter in request body") @ValidCurrency
    @Schema(example = "EUR")
    private String toCurrency;

    @NotNull(message = "Missing \"amount\" parameter in request body")
    @Positive(message = "Amount must be greater than 0")
    @Schema(example = "123.4")
    private Double amount;
}
