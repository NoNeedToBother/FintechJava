package ru.kpfu.itis.paramonov.rabbit.consumer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RabbitMqConsumer {

    private List<OnMessageReceivedListener> onMessageReceivedListeners = new ArrayList<>();

    public void addOnMessageReceivedListener(OnMessageReceivedListener onMessageReceivedListener) {
        onMessageReceivedListeners.add(onMessageReceivedListener);
    }

    public void receiveMessage(String message) {
        onMessageReceivedListeners.forEach(listener -> listener.onMessageReceived(message));
    }

    @FunctionalInterface
    public interface OnMessageReceivedListener {
        void onMessageReceived(String message);
    }
}
