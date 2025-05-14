package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.dto.ProposalCreditDto;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProducerService {

    @Value("${spring.rabbitmq.exchange.credit}")
    private String crediteExchangeName;

    private final RabbitTemplate rabbitTemplate;


    public void sendProposalToCredit(ProposalCreditDto dto) {
        log.info("Sending proposal to credit: " +  dto);
        rabbitTemplate.convertAndSend(crediteExchangeName,"credit.queue" ,dto);

    }


}
