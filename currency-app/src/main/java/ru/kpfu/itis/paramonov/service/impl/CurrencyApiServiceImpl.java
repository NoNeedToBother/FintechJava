package ru.kpfu.itis.paramonov.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.paramonov.dto.ConvertedCurrencyDto;
import ru.kpfu.itis.paramonov.dto.CurrencyRateDto;
import ru.kpfu.itis.paramonov.dto.api.CurrenciesApiResponseDto;
import ru.kpfu.itis.paramonov.dto.api.CurrencyApiResponseDto;
import ru.kpfu.itis.paramonov.exception.CurrencyCodeNotFoundException;
import ru.kpfu.itis.paramonov.service.CurrenciesApiService;
import ru.kpfu.itis.paramonov.service.CurrencyApiService;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CurrencyApiServiceImpl implements CurrencyApiService {

    private CurrenciesApiService currenciesApiService;

    private final DecimalFormat decimalFormat = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.UK));

    private static final String RUB_CODE = "RUB";

    @Override
    public CurrencyRateDto getCurrencyRate(String code) {
        var currencies = currenciesApiService.getAllCurrencies();

        return getCurrencyRateFromCurrenciesList(code, currencies);
    }

    @Override
    public ConvertedCurrencyDto convertCurrencies(String fromCurrency, String toCurrency, Double amount) {
        var currencies = currenciesApiService.getAllCurrencies();
        var currencyRateFrom = getCurrencyRateFromCurrenciesList(fromCurrency, currencies);
        var currencyRateTo = getCurrencyRateFromCurrenciesList(toCurrency, currencies);

        var result = amount * currencyRateFrom.rate() / currencyRateTo.rate();

        return new ConvertedCurrencyDto(
                currencyRateFrom.currency(), currencyRateTo.currency(),
                Double.parseDouble(decimalFormat.format(result))
        );
    }

    private CurrencyRateDto getCurrencyRateFromCurrenciesList(String code, CurrenciesApiResponseDto currencies) {
        if (code.equalsIgnoreCase(RUB_CODE)) {
            return new CurrencyRateDto(RUB_CODE, 1.0);
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
        } else {
            throw new CurrencyCodeNotFoundException("Unsupported currency code");
        }
    }
}
