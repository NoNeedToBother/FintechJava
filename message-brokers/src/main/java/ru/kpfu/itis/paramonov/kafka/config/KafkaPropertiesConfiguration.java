package ru.kpfu.itis.paramonov.kafka.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("kafka-config")
@Getter
@Setter
public class KafkaPropertiesConfiguration {

    private TopicsConfiguration topics;

    @Configuration
    @ConfigurationProperties("kafka-config.topics")
    @Getter
    @Setter
    public static class TopicsConfiguration {

        private BenchmarkTopicConfiguration benchmarkTopic;

        @Configuration
        @ConfigurationProperties("kafka-config.topics.benchmark")
        @Getter
        @Setter
        public static class BenchmarkTopicConfiguration {
            private String name;

            private int partitions;
        }
    }
}
