package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import system.dev.marques.dto.ProposalNotificationDto;

@Service
@RequiredArgsConstructor
public class ProducerService {

    @Value("${spring.rabbitmq.exchange.notification}")
    private String notificationExchangeName;

    private final RabbitTemplate rabbitTemplate;

    public void sendNotification(ProposalNotificationDto dto) {
        rabbitTemplate.convertAndSend(notificationExchangeName, "notification.queue", dto);
    }

}
