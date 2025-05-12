package system.dev.marques.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable("notification.user").build();
    }

    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange("notification.exchange");
    }

    @Bean
    public Binding bindingNotificacao() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(notificationExchange())
                .with("notification.user");
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory cf) {
        return new RabbitAdmin(cf);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> init(RabbitAdmin admin) {
        return event -> admin.initialize();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }
}
