package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProducerService {

    private String notificationExchange = "notification.exchange";

    private String routingKey = "notification.user";

    private final RabbitTemplate rabbitTemplate;

    public void enviar(String text){
        log.info("Enviando texto: " + text);
        rabbitTemplate.convertAndSend(notificationExchange, routingKey, text);
        log.info("Texto enviado: " + text);
    }

}
