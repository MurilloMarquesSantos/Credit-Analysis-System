package system.dev.marques.integrationTests;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegration.Initializer.class)
public class AbstractIntegration {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.28");
        static final RabbitMQContainer rabbit = new RabbitMQContainer("rabbitmq:3.12-management");

        static {
            Startables.deepStart(Stream.of(mysql, rabbit)).join();
        }

        @Override
        public void initialize(ConfigurableApplicationContext context) {
            ConfigurableEnvironment env = context.getEnvironment();

            Map<String, Object> props = new HashMap<>();

            // MySQL properties
            props.put("spring.datasource.url", mysql.getJdbcUrl());
            props.put("spring.datasource.username", mysql.getUsername());
            props.put("spring.datasource.password", mysql.getPassword());

            // RabbitMQ properties
            props.put("spring.rabbitmq.host", rabbit.getHost());
            props.put("spring.rabbitmq.port", rabbit.getAmqpPort());
            props.put("spring.rabbitmq.username", rabbit.getAdminUsername());
            props.put("spring.rabbitmq.password", rabbit.getAdminPassword());

            // Queues and exchanges
            props.put("spring.rabbitmq.queue.proposal", "queue.proposal");
            props.put("spring.rabbitmq.queue.notification-created", "queue.notification.user.created");
            props.put("spring.rabbitmq.queue.notification-validation", "queue.notification.user.validation");
            props.put("spring.rabbitmq.exchange.proposal", "proposal.exchange");
            props.put("spring.rabbitmq.exchange.notification", "notification.exchange");

            env.getPropertySources().addFirst(new MapPropertySource("testcontainers", props));
        }
    }
}