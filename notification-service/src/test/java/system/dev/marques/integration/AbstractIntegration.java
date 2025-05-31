package system.dev.marques.integration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegration.Initializer.class)
public class AbstractIntegration {


    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static final RabbitMQContainer rabbit = new RabbitMQContainer("rabbitmq:3.12-management");


        static {
            Startables.deepStart(Stream.of(rabbit)).join();

            ConnectionFactory factory = new ConnectionFactory();

            factory.setHost(rabbit.getHost());
            factory.setPort(rabbit.getAmqpPort());
            factory.setUsername(rabbit.getAdminUsername());
            factory.setPassword(rabbit.getAdminPassword());

            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                channel.queueDeclare("queue.notification.user.validation", true, false, false, null);
                channel.queueDeclare("queue.notification.user.created", true, false, false, null);
                channel.queueDeclare("queue.notification.user.delete", true, false, false, null);
                channel.queueDeclare("queue.proposal.status", true, false, false, null);
                channel.queueDeclare("queue.notification.receipt", true, false, false, null);
                channel.queueDeclare("queue.notification.user.confirmation", true, false, false, null);
                channel.queueDeclare("queue.notification.user.delete", true, false, false, null);
            } catch (Exception e) {
                throw new RuntimeException("Error while trying to create queues for the integration tests", e);
            }
        }

        @Override
        public void initialize(ConfigurableApplicationContext context) {
            ConfigurableEnvironment env = context.getEnvironment();

            Map<String, Object> props = new HashMap<>();

            props.put("spring.rabbitmq.host", rabbit.getHost());
            props.put("spring.rabbitmq.port", rabbit.getAmqpPort());
            props.put("spring.rabbitmq.username", rabbit.getAdminUsername());
            props.put("spring.rabbitmq.password", rabbit.getAdminPassword());

            env.getPropertySources().addFirst(new MapPropertySource("testcontainers", props));
        }
    }

}