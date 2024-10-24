package ru.kpfu.itis.paramonov.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "currency-api")
@Getter
@Setter
public class CurrencyApiConfigurationProperties {

    private String convertCurrencyUri;
}
