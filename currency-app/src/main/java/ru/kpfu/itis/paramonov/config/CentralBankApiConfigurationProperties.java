package ru.kpfu.itis.paramonov.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "central-bank-api")
@Getter
@Setter
public class CentralBankApiConfigurationProperties {
    private String mainUri;

    private String currencyUri;
}
