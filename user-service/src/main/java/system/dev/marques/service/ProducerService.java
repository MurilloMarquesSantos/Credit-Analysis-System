package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.dto.ValidUserDto;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProducerService {

    private String notificationExchange = "notification.exchange";

    private String routingKey = "notification.user";

    private final RabbitTemplate rabbitTemplate;

    public void enviar(ValidUserDto dto){
        log.info("Sending message: {} ", dto.toString());
        rabbitTemplate.convertAndSend(notificationExchange, routingKey, dto);
        log.info("Message sent: {} ", dto.toString());
    }

}
