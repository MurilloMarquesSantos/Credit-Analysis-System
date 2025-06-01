package system.dev.marques.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.queue.documentation.documentation-info}")
    private String documentQueueName;

    @Value("${spring.rabbitmq.queue.credit.analysis}")
    private String creditQueueName;

    @Value("${spring.rabbitmq.queue.notification.proposal-status}")
    private String notificationStatusQueueName;

    @Value("${spring.rabbitmq.exchange.credit}")
    private String crediteExchangeName;

    @Value("${spring.rabbitmq.exchange.proposal-notification}")
    private String notificationExchangeName;

    @Value("${spring.rabbitmq.exchange.documentation}")
    private String documentExchangeName;

    @Bean
    public Queue creditQueue() {
        return QueueBuilder.durable(creditQueueName).build();
    }



    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(notificationStatusQueueName).build();
    }

    @Bean
    public Queue documentationQueue() {
        return QueueBuilder.durable(documentQueueName).build();
    }

    @Bean
    public DirectExchange documentExchange() {
        return new DirectExchange(documentExchangeName);
    }

    @Bean
    public DirectExchange creditExchange() {
        return new DirectExchange(crediteExchangeName);
    }




    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(notificationExchangeName);
    }

    @Bean
    public Binding bindingCredit() {
        return BindingBuilder
                .bind(creditQueue())
                .to(creditExchange())
                .with("credit.queue");
    }



    @Bean
    public Binding bindingNotification() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(notificationExchange())
                .with("status.queue");
    }

    @Bean
    public Binding bindingDocumentation() {
        return BindingBuilder
                .bind(documentationQueue())
                .to(documentExchange())
                .with("document.queue");
    }


    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
