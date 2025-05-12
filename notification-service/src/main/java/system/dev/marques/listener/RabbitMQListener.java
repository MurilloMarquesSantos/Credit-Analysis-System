package system.dev.marques.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import system.dev.marques.dto.ValidUserDto;

@Service
@Log4j2
public class RabbitMQListener {

    @RabbitListener(queues = "notification.user")
    public void listen(ValidUserDto dto) {
        log.info(dto.toString());
    }
}
