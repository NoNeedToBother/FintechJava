package ru.kpfu.itis.paramonov.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.kpfu.itis.paramonov.service.CurrencyApiService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CurrencyController.class)
public class CurrencyControllerValidationTests {

    @MockBean
    private CurrencyApiService currencyApiService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        when(currencyApiService.getCurrencyRate(any())).thenReturn(null);
        when(currencyApiService.convertCurrencies(any(), any(), any())).thenReturn(null);
    }

    @Test
    public void testCodeValidation_success() throws Exception {
        //Arrange
        String code1 = "USD";
        String code2 = "RUB";
        //Act
        ResultActions result1 = mockMvc.perform(get("/api/v1/currencies/rates/{code}", code1));
        ResultActions result2 = mockMvc.perform(get("/api/v1/currencies/rates/{code}", code2));
        //Assert
        result1.andExpect(status().isOk());
        result2.andExpect(status().isOk());
    }

    @Test
    public void testCodeValidation_fail() throws Exception {
        //Arrange
        String code1 = "ABC";
        String code2 = "fgjfh";
        //Act
        ResultActions result1 = mockMvc.perform(get("/api/v1/currencies/rates/{code}", code1));
        ResultActions result2 = mockMvc.perform(get("/api/v1/currencies/rates/{code}", code2));
        //Assert
        result1.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Unknown or invalid currency code"));

        result2.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Unknown or invalid currency code"));
    }

    @Test
    public void testConversionValidation_success() throws Exception {
        //Arrange
        String requestBody1 = "{\"fromCurrency\":\"RUB\",\"toCurrency\":\"RUB\",\"amount\":100}";
        String requestBody2 = "{\"fromCurrency\":\"USD\",\"toCurrency\":\"EUR\",\"amount\":1}";
        //Act
        ResultActions result1 = mockMvc.perform(
                post("/api/v1/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody1));
        ResultActions result2 = mockMvc.perform(
                post("/api/v1/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody2));
        //Assert
        result1.andExpect(status().isOk());
        result2.andExpect(status().isOk());
    }

    @Test
    public void testConversionValidation_success_shouldFailMissingArguments() throws Exception {
        //Arrange
        String requestBody1 = "{\"toCurrency\":\"RUB\",\"amount\":100}";
        String requestBody2 = "{\"fromCurrency\":\"RUB\",\"amount\":100}";
        String requestBody3 = "{\"fromCurrency\":\"RUB\",\"toCurrency\":\"RUB\"}";
        //Act
        ResultActions result1 = mockMvc.perform(
                post("/api/v1/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody1));
        ResultActions result2 = mockMvc.perform(
                post("/api/v1/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody2));
        ResultActions result3 = mockMvc.perform(
                post("/api/v1/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody3));
        //Assert
        result1.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Missing \"fromCurrency\" parameter in request body"));

        result2.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Missing \"toCurrency\" parameter in request body"));

        result3.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Missing \"amount\" parameter in request body"));
    }

    @Test
    public void testConversionValidation_success_shouldFailIncorrectArguments() throws Exception {
        //Arrange
        String requestBody1 = "{\"fromCurrency\":\"ABC\",\"toCurrency\":\"RUB\",\"amount\":100}";
        String requestBody2 = "{\"fromCurrency\":\"RUB\",\"toCurrency\":\"ABC\",\"amount\":100}";
        String requestBody3 = "{\"fromCurrency\":\"RUB\",\"toCurrency\":\"RUB\",\"amount\":-1}";
        //Act
        ResultActions result1 = mockMvc.perform(
                post("/api/v1/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody1));
        ResultActions result2 = mockMvc.perform(
                post("/api/v1/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody2));
        ResultActions result3 = mockMvc.perform(
                post("/api/v1/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody3));
        //Assert
        result1.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Unknown or invalid currency code"));

        result2.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Unknown or invalid currency code"));

        result3.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Amount must be greater than 0"));
    }

}
