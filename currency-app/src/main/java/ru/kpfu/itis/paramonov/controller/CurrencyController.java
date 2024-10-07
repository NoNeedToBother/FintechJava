package ru.kpfu.itis.paramonov.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.paramonov.dto.ConvertCurrencyRequestDto;
import ru.kpfu.itis.paramonov.dto.ConvertedCurrencyDto;
import ru.kpfu.itis.paramonov.dto.CurrencyRateDto;
import ru.kpfu.itis.paramonov.service.CurrencyApiService;
import ru.kpfu.itis.paramonov.validation.ValidCurrency;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

    private CurrencyApiService currencyApiService;

    @GetMapping("/rates/{code}")
    public ResponseEntity<CurrencyRateDto> getCurrencyRate(
            @PathVariable @NotNull
            @ValidCurrency String code
    ) {
        CurrencyRateDto currencyRateDto = currencyApiService.getCurrencyRate(code);
        return new ResponseEntity<>(currencyRateDto, HttpStatus.OK);
    }

    @PostMapping("/convert")
    public ResponseEntity<ConvertedCurrencyDto> convertCurrencies(
            @RequestBody @NotNull @Valid ConvertCurrencyRequestDto convertCurrencyRequestDto
    ) {
        ConvertedCurrencyDto convertedCurrencyDto = currencyApiService.convertCurrencies(
                convertCurrencyRequestDto.getFromCurrency(),
                convertCurrencyRequestDto.getToCurrency(),
                convertCurrencyRequestDto.getAmount()
        );
        return new ResponseEntity<>(convertedCurrencyDto, HttpStatus.OK);
    }
}
