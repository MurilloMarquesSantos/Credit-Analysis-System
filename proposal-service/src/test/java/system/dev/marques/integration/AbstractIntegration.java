package system.dev.marques.integration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
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

            ConnectionFactory factory = new ConnectionFactory();

            factory.setHost(rabbit.getHost());
            factory.setPort(rabbit.getAmqpPort());
            factory.setUsername(rabbit.getAdminUsername());
            factory.setPassword(rabbit.getAdminPassword());

            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                channel.queueDeclare("queue.proposal.delete", true, false, false, null);
                channel.queueDeclare("queue.proposal", true, false, false, null);
                channel.queueDeclare("queue.analyzed.credit", true, false, false, null);

            } catch (Exception e) {
                throw new RuntimeException("Error while trying to create queues for the integration tests", e);
            }
        }

        @Override
        public void initialize(ConfigurableApplicationContext context) {
            ConfigurableEnvironment env = context.getEnvironment();

            Map<String, Object> props = new HashMap<>();

            props.put("spring.datasource.url", mysql.getJdbcUrl());
            props.put("spring.datasource.username", mysql.getUsername());
            props.put("spring.datasource.password", mysql.getPassword());

            props.put("spring.rabbitmq.host", rabbit.getHost());
            props.put("spring.rabbitmq.port", rabbit.getAmqpPort());
            props.put("spring.rabbitmq.username", rabbit.getAdminUsername());
            props.put("spring.rabbitmq.password", rabbit.getAdminPassword());

            props.put("spring.rabbitmq.queue.credit.analysis", "queue.credit.analysis");
            props.put("spring.rabbitmq.queue.notification.proposal-status", "queue.proposal.status");
            props.put("spring.rabbitmq.queue.documentation.documentation-info", "queue.documentation");

            props.put("spring.rabbitmq.exchange.credit", "credit.exchange");
            props.put("spring.rabbitmq.exchange.proposal-notification", "status.exchange");
            props.put("spring.rabbitmq.exchange.documentation", "document.exchange");


            env.getPropertySources().addFirst(new MapPropertySource("testcontainers", props));
        }
    }

}