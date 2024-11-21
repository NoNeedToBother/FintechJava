package ru.kpfu.itis.paramonov.rabbit.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("rabbitmq-config")
@Getter
@Setter
public class RabbitMqPropertiesConfiguration {

    private TopicsConfiguration topics;

    @Configuration
    @ConfigurationProperties("rabbitmq-config.topics")
    @Getter
    @Setter
    public static class TopicsConfiguration {

        private BenchmarkTopicConfiguration benchmarkTopic;

        @Configuration
        @ConfigurationProperties("rabbitmq-config.topics.benchmark")
        @Getter
        @Setter
        public static class BenchmarkTopicConfiguration {
            private String name;
        }
    }
}

