package ru.kpfu.itis.paramonov.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaBenchmarkConsumer {

    @KafkaListener(topics = "benchmark", groupId = "benchmark.1")
    void receiveMessage(String message) {}


}
