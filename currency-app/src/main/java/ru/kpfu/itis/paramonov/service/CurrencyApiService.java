package ru.kpfu.itis.paramonov.service;

import ru.kpfu.itis.paramonov.dto.ConvertedCurrencyDto;
import ru.kpfu.itis.paramonov.dto.CurrencyRateDto;

public interface CurrencyApiService {

    CurrencyRateDto getCurrencyRate(String code);

    ConvertedCurrencyDto convertCurrencies(String fromCurrency, String toCurrency, Double amount);
}
