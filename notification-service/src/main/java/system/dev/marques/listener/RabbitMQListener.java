package system.dev.marques.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RabbitMQListener {

    @RabbitListener(queues = "notification.user")
    public void listen(String message) {
        log.info(message);
    }
}
