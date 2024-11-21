package ru.kpfu.itis.paramonov.rabbit.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMqConsumer {

    public void receiveMessage(String message) {
        log.info(message);
    }
}
