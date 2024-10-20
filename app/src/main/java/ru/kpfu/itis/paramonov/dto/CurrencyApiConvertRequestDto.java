package ru.kpfu.itis.paramonov.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CurrencyApiConvertRequestDto {

    @JsonProperty("fromCurrency")
    private String fromCurrency;

    @JsonProperty("toCurrency")
    private String toCurrency;

    @JsonProperty("amount")
    private Double amount;
}
