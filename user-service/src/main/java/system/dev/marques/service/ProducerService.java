package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.dto.proposal.ProposalUserInfo;
import system.dev.marques.domain.dto.rabbitmq.CreatedUserDto;
import system.dev.marques.domain.dto.rabbitmq.ValidUserDto;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProducerService {

    @Value("${spring.rabbitmq.exchange.notification}")
    private String notificationExchange;

    @Value("${spring.rabbitmq.exchange.proposal}")
    private String proposalExchange;

    private final RabbitTemplate rabbitTemplate;

    public void sendValidation(ValidUserDto dto){
        rabbitTemplate.convertAndSend(notificationExchange, "notification.user.validation", dto);
    }

    public void sendCreated(CreatedUserDto dto){
        log.info(dto.toString());
        rabbitTemplate.convertAndSend(notificationExchange, "notification.user.created", dto);
    }

    public void sendProposal(ProposalUserInfo userInfo){
        rabbitTemplate.convertAndSend(proposalExchange, "proposal.queue", userInfo);
    }

}
