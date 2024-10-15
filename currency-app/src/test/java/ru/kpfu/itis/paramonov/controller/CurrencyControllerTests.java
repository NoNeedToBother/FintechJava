package ru.kpfu.itis.paramonov.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.kpfu.itis.paramonov.dto.api.CurrenciesApiResponseDto;
import ru.kpfu.itis.paramonov.dto.api.CurrencyApiResponseDto;
import ru.kpfu.itis.paramonov.exception.CurrencyApiGatewayErrorException;
import ru.kpfu.itis.paramonov.service.CurrenciesApiService;
import ru.kpfu.itis.paramonov.service.impl.CurrencyApiServiceImpl;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CurrencyController.class)
@Import(CurrencyApiServiceImpl.class)
public class CurrencyControllerTests {

    @MockBean
    private CurrenciesApiService currenciesApiService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGet_fail_shouldFailAPI() throws Exception {
        //Arrange
        String code = "RUB";
        when(currenciesApiService.getAllCurrencies()).thenThrow(CurrencyApiGatewayErrorException.class);
        //Act
        ResultActions result = mockMvc.perform(get("/api/v1/currencies/rates/{code}", code));
        //Assert
        result.andExpect(status().isServiceUnavailable())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(header().exists("Retry-After"))
                .andExpect(header().string("Retry-After", "3600"))
                .andExpect(jsonPath("$.code").value(503))
                .andExpect(jsonPath("$.message").value("Currency API unavailable"));
    }

    @Test
    public void testConversion_success() throws Exception {
        //Arrange
        double rateUSD = 12.3;
        double rateEUR = 45.6;
        int amount = 100;
        String request = "{\"fromCurrency\":\"USD\",\"toCurrency\":\"EUR\",\"amount\":100}";

        var currenciesResponse = getCurrenciesAPIResponseExampleWithUSDAndEUR(rateUSD, rateEUR);
        when(currenciesApiService.getAllCurrencies()).thenReturn(currenciesResponse);

        Double expectedFromUSDtoEUR = amount * rateUSD / rateEUR;
        var decimalFormat = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.UK));
        String expectedConvertedAmount = decimalFormat.format(expectedFromUSDtoEUR);
        //Act
        ResultActions result = mockMvc.perform(post("/api/v1/currencies/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request));
        //Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.fromCurrency").value("USD"))
                .andExpect(jsonPath("$.toCurrency").value("EUR"))
                .andExpect(jsonPath("$.convertedAmount").value(Double.valueOf(expectedConvertedAmount)));
    }

    private CurrenciesApiResponseDto getCurrenciesAPIResponseExampleWithUSDAndEUR(
            Double rateUSD, Double rateEUR
    ) {
        var currenciesResponse = new CurrenciesApiResponseDto();
        var currencyResponseUSD = new CurrencyApiResponseDto();
        ReflectionTestUtils.setField(currencyResponseUSD, "charCode", "USD");
        ReflectionTestUtils.setField(currencyResponseUSD, "rate", rateUSD.toString());

        var currencyResponseEUR = new CurrencyApiResponseDto();
        ReflectionTestUtils.setField(currencyResponseEUR, "charCode", "EUR");
        ReflectionTestUtils.setField(currencyResponseEUR, "rate", rateEUR.toString());

        List<CurrencyApiResponseDto> currencies = List.of(currencyResponseUSD, currencyResponseEUR);
        ReflectionTestUtils.setField(currenciesResponse, "currencies", currencies);
        return currenciesResponse;
    }
}
