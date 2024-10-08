package ru.kpfu.itis.paramonov.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Currency controller", description = "Provides method for interacting with currencies")
public class CurrencyController {

    private CurrencyApiService currencyApiService;

    @Operation(summary = "Get currency rate by its code",
            description = "Get latest currency rate by its ISO-4217 code",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully returned currency rate", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Invalid, incorrect or missing currency code", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Unsupported currency code", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "503", headers = @Header(name = "Retry-After", description = "3600"), description = "Failed to get data from external API", content = @Content(mediaType = "application/json"))
            })
    @GetMapping("/rates/{code}")
    public ResponseEntity<CurrencyRateDto> getCurrencyRate(
            @PathVariable @NotNull @ValidCurrency
            @Parameter(required = true, description = "ISO-4217 currency code")
            String code
    ) {
        CurrencyRateDto currencyRateDto = currencyApiService.getCurrencyRate(code);
        return new ResponseEntity<>(currencyRateDto, HttpStatus.OK);
    }

    @Operation(summary = "Convert money amount from one currency to another",
            description = "Convert specified money amount from one currency to another, each specified with ISO-4217 code",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully returned currency rate", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Invalid, incorrect or missing request body arguments", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Unsupported currency code(s)", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "503", headers = @Header(name = "Retry-After", description = "3600"), description = "Failed to get data from external API", content = @Content(mediaType = "application/json"))
            })
    @PostMapping("/convert")
    public ResponseEntity<ConvertedCurrencyDto> convertCurrencies(
            @RequestBody @NotNull @Valid
            @Parameter(required = true, description = "JSON body with desired conversion")
            ConvertCurrencyRequestDto convertCurrencyRequestDto
    ) {
        ConvertedCurrencyDto convertedCurrencyDto = currencyApiService.convertCurrencies(
                convertCurrencyRequestDto.getFromCurrency(),
                convertCurrencyRequestDto.getToCurrency(),
                convertCurrencyRequestDto.getAmount()
        );
        return new ResponseEntity<>(convertedCurrencyDto, HttpStatus.OK);
    }
}
