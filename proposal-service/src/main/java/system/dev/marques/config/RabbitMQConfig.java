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


    @Value("${spring.rabbitmq.exchange.credit}")
    private String crediteExchangeName;

    @Bean
    public Queue creditQueue() {
        return QueueBuilder.durable(creditQueueName).build();
    }

    @Bean
    public DirectExchange creditExchange() {
        return new DirectExchange(crediteExchangeName);
    }

    @Bean
    public Binding bindingCredit() {
        return BindingBuilder
                .bind(creditQueue())
                .to(creditExchange())
                .with("credit.queue");
    }


    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
