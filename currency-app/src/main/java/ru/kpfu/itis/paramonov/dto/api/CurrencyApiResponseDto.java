package ru.kpfu.itis.paramonov.dto.api;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;

@Getter
public class CurrencyApiResponseDto {

    @JacksonXmlProperty(localName = "CharCode")
    private String charCode;

    @JacksonXmlProperty(localName = "VunitRate")
    private String rate;
}
