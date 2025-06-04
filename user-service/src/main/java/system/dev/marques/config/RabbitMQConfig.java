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
    private String notificationCreateQueueName;

    @Value("${spring.rabbitmq.queue.notification-validation}")
    private String notificationValidationQueueName;

    @Value("${spring.rabbitmq.queue.notification-delete-user}")
    private String notificationDeleteQueueName;

    @Value("${spring.rabbitmq.queue.notification-delete-confirmation}")
    private String notificationConfirmationQueueName;

    @Value("${spring.rabbitmq.queue.documentation.receipt}")
    private String userReceiptQueueName;

    @Value("${spring.rabbitmq.queue.credit.analyzed-credit}")
    private String analyzedCreditQueueName;

    @Value("${spring.rabbitmq.queue.proposal-delete}")
    private String proposalDeleteQueueName;

    @Value("${spring.rabbitmq.queue.notification.proposal-receipt}")
    private String notificationQueueName;

    @Value("${spring.rabbitmq.queue.documentation.documentation-info}")
    private String documentQueueName;

    @Value("${spring.rabbitmq.queue.credit.analysis}")
    private String creditQueueName;

    @Value("${spring.rabbitmq.queue.notification.proposal-status}")
    private String notificationStatusQueueName;

    @Value("${spring.rabbitmq.exchange.analyzed-credit}")
    private String analyzedCreditExchangeName;

    @Value("${spring.rabbitmq.exchange.notification-receipt}")
    private String notificationExchangeName;

    @Value("${spring.rabbitmq.exchange.credit}")
    private String crediteExchangeName;

    @Value("${spring.rabbitmq.exchange.proposal-notification}")
    private String notificationExchangeNameProposal;

    @Value("${spring.rabbitmq.exchange.documentation-document}")
    private String documentExchangeName;

    @Value("${spring.rabbitmq.exchange.notification}")
    private String notificationExchange;

    @Value("${spring.rabbitmq.exchange.proposal}")
    private String proposalExchangeName;

    @Value("${spring.rabbitmq.exchange.documentation}")
    private String documentationExchangeName;

    @Bean
    public Queue userValidationQueue() {
        return QueueBuilder.durable(notificationValidationQueueName).build();
    }

    @Bean
    public Queue userCreatedQueue() {
        return QueueBuilder.durable(notificationCreateQueueName).build();
    }

    @Bean
    public Queue proposalDeleteQueue() {
        return QueueBuilder.durable(proposalDeleteQueueName).build();
    }

    @Bean
    public Queue userDeleteQueue() {
        return QueueBuilder.durable(notificationDeleteQueueName).build();
    }

    @Bean
    public Queue userConfirmationQueue() {
        return QueueBuilder.durable(notificationConfirmationQueueName).build();
    }

    @Bean
    public Queue proposalQueue() {return QueueBuilder.durable(proposalQueueName).build();}

    @Bean
    public Queue documentationQueue() {
        return QueueBuilder.durable(userReceiptQueueName).build();
    }

    @Bean
    public Queue documentationDeletionQueue() {
        return QueueBuilder.durable(documentDeletionQueueName).build();
    }

    @Bean
    public Queue analyzedCreditQueue() {
        return QueueBuilder.durable(analyzedCreditQueueName).build();
    }

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(notificationQueueName).build();
    }

    @Bean
    public Queue creditQueueProposal() {return QueueBuilder.durable(creditQueueName).build();}

    @Bean
    public Queue notificationQueueProposal() {return QueueBuilder.durable(notificationStatusQueueName).build();}

    @Bean
    public Queue documentationQueueProposal() {return QueueBuilder.durable(documentQueueName).build();}

    @Bean
    public DirectExchange documentExchangeProposal() {return new DirectExchange(documentExchangeName);}

    @Bean
    public DirectExchange creditExchangeProposal() {return new DirectExchange(crediteExchangeName);}

    @Bean
    public DirectExchange notificationExchangeProposal() {return new DirectExchange(notificationExchangeNameProposal);}

    @Bean
    public DirectExchange analyzedCreditExchange() {return new DirectExchange(analyzedCreditExchangeName);}

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
    public DirectExchange notificationExchange() {
        return new DirectExchange(notificationExchangeName);
    }


    @Bean
    public Binding bindingAnalyzedCredit() {
        return BindingBuilder
                .bind(analyzedCreditQueue())
                .to(analyzedCreditExchange())
                .with("analyzed-credit.queue");
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
    public Binding notificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(notificationExchange())
                .with("notification.queue");
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
