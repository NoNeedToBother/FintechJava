package ru.kpfu.itis.paramonov.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@RequiredArgsConstructor
public class NetworkConfig {

    private final KudaGoApiConfigurationProperties kudaGoApiConfigurationProperties;

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean("kudago_api_data_initializer")
    public ExecutorService kudaGoThreadPoolDataInitializer() {
        var threadAmount = kudaGoApiConfigurationProperties.getExecutorsConfig().getDataInitializerThreadAmount();
        return Executors.newFixedThreadPool(threadAmount,
                new CustomizableThreadFactory("data-initializer-thread-"));
    }

    @Bean("kudago_api_data_scheduler")
    public ScheduledExecutorService kudaGoScheduledThreadPoolDataScheduler() {
        return Executors.newScheduledThreadPool(1,
                new CustomizableThreadFactory("data-scheduler-thread-"));
    }

    @Bean("kudago_api_event_data_executor_service")
    public ExecutorService kudaGoThreadPoolEventDataGetter() {
        return Executors.newFixedThreadPool(10,
                new CustomizableThreadFactory("event-data-executor-service-thread-"));
    }
}
