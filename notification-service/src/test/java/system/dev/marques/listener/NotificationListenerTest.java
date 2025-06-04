package system.dev.marques.listener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import system.dev.marques.dto.*;
import system.dev.marques.service.EmailService;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.doNothing;
import static system.dev.marques.util.QueueDtoCreator.*;

@ExtendWith(MockitoExtension.class)
class NotificationListenerTest {

    @InjectMocks
    private NotificationListener listener;

    @Mock
    private EmailService emailService;


    @Test
    void listenValidationQueue_sendUserValidationLink_WhenSuccessful() {
        doNothing().when(emailService).sendUserValidationLink(any(ValidUserDto.class));
        assertThatCode(() -> listener.listenValidationQueue(createValidUserDto())).doesNotThrowAnyException();
    }

    @Test
    void listenCreateQueue_sendUserCreatedEmail_WhenSuccessful() {
        doNothing().when(emailService).sendUserCreatedEmail(any(CreatedUserDto.class));
        assertThatCode(() -> listener.listenCreateQueue(createCreatedUserDto())).doesNotThrowAnyException();
    }

    @Test
    void listenDeleteQueue_sendUserDeleteFormEmail_WhenSuccessful() {
        doNothing().when(emailService).sendUserDeleteFormEmail(any(DeleteUserDto.class));
        assertThatCode(() -> listener.listenDeleteQueue(createDeleteUserDto())).doesNotThrowAnyException();
    }

    @Test
    void listenProposalStatusQueue_sendProposalStatusEmail_WhenSuccessful() {
        doNothing().when(emailService).sendProposalStatusEmail(any(ProposalStatusEmailDto.class));
        assertThatCode(() -> listener
                .listenProposalStatusQueue(createProposalStatusEmailDto())).doesNotThrowAnyException();
    }

    @Test
    void listenProposalReceiptQueue_sendProposalReceiptUrl_WhenSuccessful() {
        doNothing().when(emailService).sendProposalReceiptUrl(any(ProposalNotificationDto.class));
        assertThatCode(() -> listener
                .listenProposalReceiptQueue(createProposalNotificationDto())).doesNotThrowAnyException();
    }

    @Test
    void listenUserDeleteConfirmation_sendDeletionConfirmation_WhenSuccessful() {
        doNothing().when(emailService).sendDeletionConfirmation(any(DeleteUserConfirmationDto.class));
        assertThatCode(() -> listener
                .listenUserDeleteConfirmation(createDeleteUserConfirmationDto())).doesNotThrowAnyException();
    }
}