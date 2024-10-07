package ru.kpfu.itis.paramonov.controller;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.paramonov.dto.CurrencyRateDto;
import ru.kpfu.itis.paramonov.service.CurrencyApiService;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

    private CurrencyApiService currencyApiService;

    @GetMapping("/rates/{code}")
    public ResponseEntity<CurrencyRateDto> get(
            @PathVariable @NotNull
            @Length(min = 3, max = 3, message = "Code should follow ISO 4218 standard") String code
    ) {
        CurrencyRateDto currencyRateDto = currencyApiService.getCurrencyRate(code);
        return new ResponseEntity<>(currencyRateDto, HttpStatus.OK);
    }
}
