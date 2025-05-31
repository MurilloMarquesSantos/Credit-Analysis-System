package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.dto.proposal.ProposalUserInfo;
import system.dev.marques.domain.dto.rabbitmq.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProducerService {

    @Value("${spring.rabbitmq.exchange.notification}")
    private String notificationExchange;

    @Value("${spring.rabbitmq.exchange.proposal}")
    private String proposalExchange;

    @Value("${spring.rabbitmq.exchange.documentation}")
    private String documentationExchange;

    private final RabbitTemplate rabbitTemplate;

    public void sendValidation(ValidUserDto dto) {
        rabbitTemplate.convertAndSend(notificationExchange, "notification.user.validation", dto);
    }

    public void sendCreated(CreatedUserDto dto) {
        rabbitTemplate.convertAndSend(notificationExchange, "notification.user.created", dto);
    }

    public void sendProposal(ProposalUserInfo userInfo) {
        rabbitTemplate.convertAndSend(proposalExchange, "proposal.queue", userInfo);
    }

    public void sendUserReceipt(UserReceiptDto dto) {
        rabbitTemplate.convertAndSend(documentationExchange, "documentation.user", dto);
    }

    public void sendProposalDeletion(Long userId) {
        rabbitTemplate.convertAndSend(proposalExchange, "proposal.delete", userId);
    }

    public void sendDocumentDeletion(Long id) {
        rabbitTemplate.convertAndSend(documentationExchange, "documentation.deletion", id);
    }

    public void sendDeleteDto(DeleteUserDto dto) {
        rabbitTemplate.convertAndSend(notificationExchange, "notification.user.delete", dto);
    }

    public void sendUserDeleteConfirmation(DeleteUserConfirmationDto dto){
        rabbitTemplate.convertAndSend(notificationExchange, "notification.user.confirmation", dto);
    }
}
