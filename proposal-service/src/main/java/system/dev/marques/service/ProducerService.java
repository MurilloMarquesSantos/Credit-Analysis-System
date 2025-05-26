package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.dto.ApprovedProposalDto;
import system.dev.marques.domain.dto.ProposalCreditDto;
import system.dev.marques.domain.dto.ProposalStatusEmailDto;

@Service
@RequiredArgsConstructor
public class ProducerService {

    @Value("${spring.rabbitmq.exchange.credit}")
    private String crediteExchangeName;

    @Value("${spring.rabbitmq.exchange.proposal-notification}")
    private String notificationExchangeName;

    @Value("${spring.rabbitmq.exchange.documentation}")
    private String documentationExchangeName;

    private final RabbitTemplate rabbitTemplate;


    public void sendProposalToCredit(ProposalCreditDto dto) {
        rabbitTemplate.convertAndSend(crediteExchangeName, "credit.queue", dto);

    }

    public void sendProposalStatus(ProposalStatusEmailDto dto) {
        rabbitTemplate.convertAndSend(notificationExchangeName, "status.queue", dto);
    }

    public void sendApprovedProposal(ApprovedProposalDto dto) {
        rabbitTemplate.convertAndSend(documentationExchangeName, "document.queue", dto);
    }


}
