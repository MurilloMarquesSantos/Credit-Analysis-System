package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.dto.ProposalCreditDto;

@Service
@RequiredArgsConstructor
public class ProducerService {

    @Value("${spring.rabbitmq.exchange.credit}")
    private String crediteExchangeName;

    private final RabbitTemplate rabbitTemplate;

    public void sendProposalToCredit(ProposalCreditDto dto) {
        rabbitTemplate.convertAndSend(crediteExchangeName,"credit.queue" ,dto);

    }
}
