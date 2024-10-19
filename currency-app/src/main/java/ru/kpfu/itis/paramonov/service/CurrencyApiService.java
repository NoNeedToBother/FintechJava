package ru.kpfu.itis.paramonov.service;

import ru.kpfu.itis.paramonov.dto.ConvertCurrencyRequestDto;
import ru.kpfu.itis.paramonov.dto.ConvertedCurrencyDto;
import ru.kpfu.itis.paramonov.dto.CurrencyRateDto;

public interface CurrencyApiService {

    CurrencyRateDto getCurrencyRate(String code);

    ConvertedCurrencyDto convertCurrencies(ConvertCurrencyRequestDto convertCurrencyRequestDto);
}
