package ru.kpfu.itis.paramonov.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.paramonov.dto.CurrencyRateDto;
import ru.kpfu.itis.paramonov.dto.api.CurrencyApiResponseDto;
import ru.kpfu.itis.paramonov.service.CurrenciesApiService;
import ru.kpfu.itis.paramonov.service.CurrencyApiService;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CurrencyApiServiceImpl implements CurrencyApiService {

    private CurrenciesApiService currenciesApiService;

    @Override
    public CurrencyRateDto getCurrencyRate(String code) {
        var currencies = currenciesApiService.getAllCurrencies();
        if (currencies == null) {
            throw new RuntimeException();
        }

        Optional<CurrencyApiResponseDto> currencyOptional =
                currencies.getCurrencies()
                .stream()
                .filter(response -> response.getCharCode().equals(code.toUpperCase()))
                .findFirst();

        if (currencyOptional.isPresent()) {
            return new CurrencyRateDto(
                    currencyOptional.get().getCharCode(),
                    Double.valueOf(currencyOptional.get().getRate().replace(",", "."))
            );
        }
        return null;
    }
}
