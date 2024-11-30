package ru.kpfu.itis.paramonov.rabbit.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kpfu.itis.paramonov.rabbit.consumer.RabbitMqConsumer;

@Configuration
public class RabbitMqConfiguration {

    @Autowired
    private RabbitMqPropertiesConfiguration rabbitConfig;

    @Bean
    public Queue queue() {
        return new Queue("queue", false);
    }

    @Bean
    public TopicExchange rabbitBenchmarkTopic() {
        return new TopicExchange(rabbitConfig.getTopics().getBenchmarkTopic().getName());
    }

    @Bean
    public Binding rabbitBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("benchmark.#");
    }

    @Bean
    public SimpleMessageListenerContainer rabbitContainer(ConnectionFactory connectionFactory,
                                                    MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("queue");
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter rabbitListenerAdapter(RabbitMqConsumer consumer) {
        return new MessageListenerAdapter(consumer, "receiveMessage");
    }

}
