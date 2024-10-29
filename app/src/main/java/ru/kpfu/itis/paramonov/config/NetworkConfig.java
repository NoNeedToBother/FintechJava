package ru.kpfu.itis.paramonov.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;

@Configuration
@RequiredArgsConstructor
public class NetworkConfig {

    private final KudaGoApiConfigurationProperties kudaGoApiConfigurationProperties;

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean("kudagoApiDataInitializer")
    public ExecutorService kudaGoThreadPoolDataInitializer() {
        var threadAmount = kudaGoApiConfigurationProperties.getExecutorsConfig().getDataInitializerThreadAmount();
        return Executors.newFixedThreadPool(threadAmount,
                new CustomizableThreadFactory("data-initializer-thread-"));
    }

    @Bean("kudagoApiDataScheduler")
    public ScheduledExecutorService kudaGoScheduledThreadPoolDataScheduler() {
        return Executors.newScheduledThreadPool(1,
                new CustomizableThreadFactory("data-scheduler-thread-"));
    }

    @Bean("kudagoApiEventRateLimiter")
    public Semaphore eventRateLimiter() {
        return new Semaphore(kudaGoApiConfigurationProperties.getEventRateLimit());
    }
}
