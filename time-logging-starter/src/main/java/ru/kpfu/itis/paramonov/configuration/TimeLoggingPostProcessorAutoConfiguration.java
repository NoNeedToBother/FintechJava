package ru.kpfu.itis.paramonov.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kpfu.itis.paramonov.configuration.time.LogTimePostProcessor;

@Configuration
public class TimeLoggingPostProcessorAutoConfiguration {

    @Bean
    public LogTimePostProcessor logTimePostProcessor() {
        return new LogTimePostProcessor();
    }
}
