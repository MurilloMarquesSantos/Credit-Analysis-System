package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import system.dev.marques.dto.AnalyzedDto;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange.analyzed-credit}")
    private String exchangeName;

    public void sendAnalyzedProposal(AnalyzedDto dto) {
        rabbitTemplate.convertAndSend(exchangeName,"analyzed-credit.queue", dto);

    }


}
