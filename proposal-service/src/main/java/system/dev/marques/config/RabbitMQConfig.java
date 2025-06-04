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
    public Queue creditQueueProposal() {
        return QueueBuilder.durable(creditQueueName).build();
    }

    @Bean
    public Queue notificationQueueProposal() {
        return QueueBuilder.durable(notificationStatusQueueName).build();
    }

    @Bean
    public Queue documentationQueueProposal() {
        return QueueBuilder.durable(documentQueueName).build();
    }

    @Bean
    public DirectExchange documentExchangeProposal() {
        return new DirectExchange(documentExchangeName);
    }

    @Bean
    public DirectExchange creditExchangeProposal() {
        return new DirectExchange(crediteExchangeName);
    }

    @Bean
    public DirectExchange notificationExchangeProposal() {
        return new DirectExchange(notificationExchangeName);
    }

    @Bean
    public Binding bindingCreditProposal() {
        return BindingBuilder
                .bind(creditQueueProposal())
                .to(creditExchangeProposal())
                .with("credit.queue");
    }

    @Bean
    public Binding bindingNotificationProposal() {
        return BindingBuilder
                .bind(notificationQueueProposal())
                .to(notificationExchangeProposal())
                .with("status.queue");
    }

    @Bean
    public Binding bindingDocumentationProposal() {
        return BindingBuilder
                .bind(documentationQueueProposal())
                .to(documentExchangeProposal())
                .with("document.queue");
    }


    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
