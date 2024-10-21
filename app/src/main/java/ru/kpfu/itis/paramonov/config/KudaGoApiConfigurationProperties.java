package ru.kpfu.itis.paramonov.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "kudago-api")
@Getter
@Setter
public class KudaGoApiConfigurationProperties {

    private String citiesUri;

    private String categoriesUri;

    private String eventsUri;

    private KudaGoApiConfigurationExecutorsProperties executorsConfig;

    private int eventRateLimit;

    @Configuration
    @ConfigurationProperties(prefix = "kudago-api.executors-config")
    @Getter
    @Setter
    public static class KudaGoApiConfigurationExecutorsProperties {

        private int dataInitializerThreadAmount;

        private Duration dataSchedulerDuration;

        private int eventDataGetterThreadAmount;

    }

}
