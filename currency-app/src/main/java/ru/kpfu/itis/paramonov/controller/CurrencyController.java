package ru.kpfu.itis.paramonov.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.paramonov.api.CurrencyApi;
import ru.kpfu.itis.paramonov.dto.ConvertCurrencyRequestDto;
import ru.kpfu.itis.paramonov.dto.ConvertedCurrencyDto;
import ru.kpfu.itis.paramonov.dto.CurrencyRateDto;
import ru.kpfu.itis.paramonov.service.CurrencyApiService;

@RestController
@AllArgsConstructor
public class CurrencyController implements CurrencyApi {

    private CurrencyApiService currencyApiService;

    @Override
    public ResponseEntity<CurrencyRateDto> getCurrencyRate(String code) {
        CurrencyRateDto currencyRateDto = currencyApiService.getCurrencyRate(code);
        return new ResponseEntity<>(currencyRateDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ConvertedCurrencyDto> convertCurrencies(
            ConvertCurrencyRequestDto convertCurrencyRequestDto
    ) {
        ConvertedCurrencyDto convertedCurrencyDto = currencyApiService.convertCurrencies(
                convertCurrencyRequestDto
        );
        return new ResponseEntity<>(convertedCurrencyDto, HttpStatus.OK);
    }
}
