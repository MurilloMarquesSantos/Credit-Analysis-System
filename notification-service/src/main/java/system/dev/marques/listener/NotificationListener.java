package system.dev.marques.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import system.dev.marques.dto.*;
import system.dev.marques.service.EmailService;

@Service
@Log4j2
@RequiredArgsConstructor
public class NotificationListener {

    private final EmailService emailService;

    @RabbitListener(queues = "queue.notification.user.validation")
    public void listenValidationQueue(ValidUserDto dto) {
        emailService.sendUserValidationLink(dto);
    }

    @RabbitListener(queues = "queue.notification.user.created")
    public void listenCreateQueue(CreatedUserDto dto) {
        emailService.sendUserCreatedEmail(dto);
    }

    @RabbitListener(queues = "queue.notification.user.delete")
    public void listenDeleteQueue(DeleteUserDto dto) {
        emailService.sendUserDeleteFormEmail(dto);
    }

    @RabbitListener(queues = "queue.proposal.status")
    public void listenProposalStatusQueue(ProposalStatusEmailDto dto) {
        emailService.sendProposalStatusEmail(dto);
    }

    @RabbitListener(queues = "queue.notification.receipt")
    public void listenProposalReceiptQueue(ProposalNotificationDto dto) {
        emailService.sendProposalReceiptUrl(dto);
    }

    @RabbitListener(queues = "queue.notification.user.receipt")
    public void listenUserReceiptQueue(ProposalNotificationDto dto) {
        emailService.sendProposalReceiptUrl(dto);
    }

    @RabbitListener(queues = "queue.notification.user.confirmation")
    public void listenUserDeleteConfirmation(DeleteUserConfirmationDto dto) {
        emailService.sendDeletionConfirmation(dto);
    }
}
