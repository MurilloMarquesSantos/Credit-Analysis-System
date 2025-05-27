package system.dev.marques.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.queue.notification.proposal-receipt}")
    private String notificationQueueName;

    @Value("${spring.rabbitmq.queue.notification.user-receipt}")
    private String userReceiptQueueName;

    @Value("${spring.rabbitmq.exchange.notification}")
    private String notificationExchangeName;

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(notificationQueueName).build();
    }

    @Bean
    public Queue userReceiptQueue() {
        return QueueBuilder.durable(userReceiptQueueName).build();
    }

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(notificationExchangeName);
    }

    @Bean
    public Binding notificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(notificationExchange())
                .with("notification.queue");
    }

    @Bean
    public Binding userReceiptBinding() {
        return BindingBuilder
                .bind(userReceiptQueue())
                .to(notificationExchange())
                .with("notification.user");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
