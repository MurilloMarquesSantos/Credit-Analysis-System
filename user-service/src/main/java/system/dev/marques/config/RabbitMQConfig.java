package system.dev.marques.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.queue.proposal}")
    private String proposalQueueName;

    @Value("${spring.rabbitmq.queue.documentation.deletion}")
    private String documentDeletionQueueName;

    @Value("${spring.rabbitmq.queue.notification-created}")
    private String notificationCreateQueue;

    @Value("${spring.rabbitmq.queue.notification-validation}")
    private String notificationValidationQueue;

    @Value("${spring.rabbitmq.queue.notification-delete-user}")
    private String notificationDeleteQueue;

    @Value("${spring.rabbitmq.queue.notification-delete-confirmation}")
    private String notificationConfirmationQueue;

    @Value("${spring.rabbitmq.exchange.notification}")
    private String notificationExchange;

    @Value("${spring.rabbitmq.exchange.proposal}")
    private String proposalExchangeName;

    @Value("${spring.rabbitmq.queue.documentation.receipt}")
    private String userReceiptQueueName;

    @Value("${spring.rabbitmq.exchange.documentation}")
    private String documentationExchangeName;

    @Value("${spring.rabbitmq.queue.proposal-delete}")
    private String proposalDeleteQueueName;

    @Bean
    public Queue userValidationQueue() {
        return QueueBuilder.durable(notificationValidationQueue).build();
    }

    @Bean
    public Queue userCreatedQueue() {
        return QueueBuilder.durable(notificationCreateQueue).build();
    }

    @Bean
    public Queue proposalDeleteQueue() {
        return QueueBuilder.durable(proposalDeleteQueueName).build();
    }
    @Bean
    public Queue userDeleteQueue() {
        return QueueBuilder.durable(notificationDeleteQueue).build();
    }

    @Bean
    public Queue userConfirmationQueue() {
        return QueueBuilder.durable(notificationConfirmationQueue).build();
    }

    @Bean
    public Queue proposalQueue() {
        return QueueBuilder.durable(proposalQueueName).build();
    }

    @Bean
    public Queue documentationQueue() {
        return QueueBuilder.durable(userReceiptQueueName).build();
    }

    @Bean
    public Queue documentationDeletionQueue() {
        return QueueBuilder.durable(documentDeletionQueueName).build();
    }

    @Bean
    public TopicExchange documentationExchange() {
        return new TopicExchange(documentationExchangeName);
    }

    @Bean
    public TopicExchange notificationTopicExchange() {
        return new TopicExchange(notificationExchange);
    }

    @Bean
    public TopicExchange proposalExchange() {
        return new TopicExchange(proposalExchangeName);
    }

    @Bean
    public Binding documentationBinding() {
        return BindingBuilder
                .bind(documentationQueue())
                .to(documentationExchange())
                .with("documentation.user");
    }

    @Bean
    public Binding documentationDeleteBinding() {
        return BindingBuilder
                .bind(documentationDeletionQueue())
                .to(documentationExchange())
                .with("documentation.deletion");
    }

    @Bean
    public Binding bindingProposal() {
        return BindingBuilder
                .bind(proposalQueue())
                .to(proposalExchange())
                .with("proposal.queue");
    }

    @Bean
    public Binding bindingProposalDelete() {
        return BindingBuilder
                .bind(proposalDeleteQueue())
                .to(proposalExchange())
                .with("proposal.delete");
    }

    @Bean
    public Binding bindingNotificationValidation() {
        return BindingBuilder
                .bind(userValidationQueue())
                .to(notificationTopicExchange())
                .with("notification.user.validation");
    }

    @Bean
    public Binding bindingNotificationCreated() {
        return BindingBuilder
                .bind(userCreatedQueue())
                .to(notificationTopicExchange())
                .with("notification.user.created");
    }

    @Bean
    public Binding bindingNotificationDelete() {
        return BindingBuilder
                .bind(userDeleteQueue())
                .to(notificationTopicExchange())
                .with("notification.user.delete");
    }

    @Bean
    public Binding bindingNotificationConfirmation() {
        return BindingBuilder
                .bind(userConfirmationQueue())
                .to(notificationTopicExchange())
                .with("notification.user.confirmation");
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory cf) {
        return new RabbitAdmin(cf);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent>
    init(RabbitAdmin admin) {
        return event -> admin.initialize();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }
}
