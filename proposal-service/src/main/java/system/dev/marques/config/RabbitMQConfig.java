package system.dev.marques.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.queue.credit.analysis}")
    private String creditQueueName;


    @Value("${spring.rabbitmq.queue.credit.analyzed-credit}")
    private String analyzedCreditQueueName;

    @Value("${spring.rabbitmq.exchange.credit}")
    private String crediteExchangeName;

    @Value("${spring.rabbitmq.exchange.analyzed-credit}")
    private String analyzedCreditExchangeName;

    @Bean
    public Queue creditQueue() {
        return QueueBuilder.durable(creditQueueName).build();
    }


    @Bean
    public Queue analyzedCreditQueue() {
        return QueueBuilder.durable(analyzedCreditQueueName).build();
    }

    @Bean
    public DirectExchange creditExchange() {
        return new DirectExchange(crediteExchangeName);
    }


    @Bean
    public DirectExchange analyzedCreditExchange() {
        return new DirectExchange(analyzedCreditExchangeName);
    }

    @Bean
    public Binding bindingCredit() {
        return BindingBuilder
                .bind(creditQueue())
                .to(creditExchange())
                .with("credit.queue");
    }

    @Bean
    public Binding bindingAnalyzedCredit() {
        return BindingBuilder
                .bind(analyzedCreditQueue())
                .to(analyzedCreditExchange())
                .with("analyzed-credit.queue");
    }


    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
