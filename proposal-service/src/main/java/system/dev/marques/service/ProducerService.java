package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.dto.ProposalCreditDto;
import system.dev.marques.domain.dto.ProposalStatusEmailDto;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProducerService {

    @Value("${spring.rabbitmq.exchange.credit}")
    private String crediteExchangeName;

    @Value("${spring.rabbitmq.exchange.proposal-notification}")
    private String notificationExchangeName;

    private final RabbitTemplate rabbitTemplate;


    public void sendProposalToCredit(ProposalCreditDto dto) {
        log.info("Sending proposal to credit: " + dto);
        rabbitTemplate.convertAndSend(crediteExchangeName, "credit.queue", dto);

    }

    public void sendProposalStatus(ProposalStatusEmailDto dto) {
        log.info(dto.toString());
        rabbitTemplate.convertAndSend(notificationExchangeName, "status.queue", dto);
    }


}
