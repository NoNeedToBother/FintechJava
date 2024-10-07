package ru.kpfu.itis.paramonov.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import ru.kpfu.itis.paramonov.validation.ValidCurrency;

@Getter
public class ConvertCurrencyRequestDto {

    @NotNull(message = "Missing \"fromCurrency\" parameter in request body") @ValidCurrency
    private String fromCurrency;

    @NotNull(message = "Missing \"toCurrency\" parameter in request body") @ValidCurrency
    private String toCurrency;

    @NotNull(message = "Missing \"amount\" parameter in request body") @Positive
    private Double amount;
}
