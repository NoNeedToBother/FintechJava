package ru.kpfu.itis.paramonov.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.kpfu.itis.paramonov.validation.ValidCurrency;

@Getter
@AllArgsConstructor
public class GetEventsRequestDto {

    @NotNull(message = "Missing budget parameter in request parameters")
    private Double budget;

    @ValidCurrency
    @NotNull(message = "Missing currency parameter in request parameters")
    private String currency;

    private String dateFrom;

    private String dateTo;
}
