package system.dev.marques.integration.listener;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import system.dev.marques.NotificationApplication;
import system.dev.marques.dto.*;
import system.dev.marques.integration.AbstractIntegration;
import system.dev.marques.integration.config.MockConfig;
import system.dev.marques.service.EmailService;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static system.dev.marques.util.QueueDtoCreator.*;

@SpringBootTest(classes = {NotificationApplication.class, MockConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "server.port=9595")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class NotificationListenerIT extends AbstractIntegration {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private EmailService emailService;

    @Test
    void shouldListenToValidationQueueAndSendEmail() {

        ValidUserDto dto = createValidUserDto();

        rabbitTemplate.convertAndSend("", "queue.notification.user.validation", dto);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(emailService, times(1)).sendUserValidationLink(dto));

    }

    @Test
    void shouldListenToCreatedQueueAndSendEmail() {

        CreatedUserDto dto = createCreatedUserDto();

        rabbitTemplate.convertAndSend("", "queue.notification.user.created", dto);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(emailService, times(1)).sendUserCreatedEmail(dto));

    }

    @Test
    void shouldListenToDeleteQueueAndSendEmail() {

        DeleteUserDto dto = createDeleteUserDto();

        rabbitTemplate.convertAndSend("", "queue.notification.user.delete", dto);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(emailService, times(1)).sendUserDeleteFormEmail(dto));

    }

    @Test
    void shouldListenToStatusQueueAndSendEmail() {

        ProposalStatusEmailDto dto = createProposalStatusEmailDto();

        rabbitTemplate.convertAndSend("", "queue.proposal.status", dto);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(emailService, times(1)).sendProposalStatusEmail(dto));

    }

    @Test
    void shouldListenToNotificationUserQueueAndSendEmail() {

        ProposalNotificationDto dto = createProposalNotificationDto();

        rabbitTemplate.convertAndSend("", "queue.notification.receipt", dto);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(emailService, times(1)).sendProposalReceiptUrl(dto));

    }

    @Test
    void shouldListenToUserConfirmationQueueAndSendEmail() {

        DeleteUserConfirmationDto dto = createDeleteUserConfirmationDto();

        rabbitTemplate.convertAndSend("", "queue.notification.user.confirmation", dto);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(emailService, times(1)).sendDeletionConfirmation(dto));

    }

}
