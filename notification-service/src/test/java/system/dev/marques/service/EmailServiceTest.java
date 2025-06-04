package system.dev.marques.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import system.dev.marques.dto.*;
import system.dev.marques.exception.EmailSendingException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.*;
import static system.dev.marques.util.QueueDtoCreator.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSenderMock;

    private static final String ERROR_MESSAGE = "Unable to send email: ";

    @BeforeEach
    void setUp() {

        when(mailSenderMock.createMimeMessage()).thenReturn(mock(MimeMessage.class));
    }

    @Test
    void sendUserValidationLink_SendsEmail_WhenSuccessful() {
        ValidUserDto dto = createValidUserDto();

        assertThatCode(() -> emailService.sendUserValidationLink(dto)).doesNotThrowAnyException();

        verify(mailSenderMock, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void sendUserValidationLink_ThrowsEmailSendingException_WhenFailed() {

        ValidUserDto dto = createValidUserDto();

        dto.setEmail("");

        assertThatExceptionOfType(EmailSendingException.class)
                .isThrownBy(() -> emailService.sendUserValidationLink(dto))
                .withMessageContaining(ERROR_MESSAGE);

    }

    @Test
    void sendUserCreatedEmail_SendsEmail_WhenSuccessful() {
        CreatedUserDto dto = createCreatedUserDto();

        assertThatCode(() -> emailService.sendUserCreatedEmail(dto)).doesNotThrowAnyException();

        verify(mailSenderMock, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void sendUserCreatedEmail_ThrowsEmailSendingException_WhenFailed() {
        CreatedUserDto dto = createCreatedUserDto();

        dto.setEmail("");

        assertThatExceptionOfType(EmailSendingException.class)
                .isThrownBy(() -> emailService.sendUserCreatedEmail(dto))
                .withMessageContaining(ERROR_MESSAGE);
    }

    @Test
    void sendUserDeleteFormEmail_SendsEmail_WhenSuccessful() {
        ReflectionTestUtils.setField(emailService, "email", "email@test.com");

        DeleteUserDto dto = createDeleteUserDto();

        assertThatCode(() -> emailService.sendUserDeleteFormEmail(dto)).doesNotThrowAnyException();

        verify(mailSenderMock, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void sendUserDeleteFormEmail_ThrowsEmailSendingException_WhenFailed() {
        ReflectionTestUtils.setField(emailService, "email", "");

        DeleteUserDto dto = createDeleteUserDto();

        dto.setUserEmail("");

        assertThatExceptionOfType(EmailSendingException.class)
                .isThrownBy(() -> emailService.sendUserDeleteFormEmail(dto))
                .withMessageContaining(ERROR_MESSAGE);
    }

    @Test
    void sendDeletionConfirmation_SendsEmail_WhenSuccessful() {
        DeleteUserConfirmationDto dto = createDeleteUserConfirmationDto();

        assertThatCode(() -> emailService.sendDeletionConfirmation(dto)).doesNotThrowAnyException();

        verify(mailSenderMock, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void sendDeletionConfirmation_ThrowsEmailSendingException_WhenFailed() {
        DeleteUserConfirmationDto dto = createDeleteUserConfirmationDto();

        dto.setUserEmail("");

        assertThatExceptionOfType(EmailSendingException.class)
                .isThrownBy(() -> emailService.sendDeletionConfirmation(dto))
                .withMessageContaining(ERROR_MESSAGE);
    }

    @Test
    void sendProposalStatusEmail_SendsEmailApproved_WhenSuccessful() {

        ProposalStatusEmailDto dto = createProposalStatusEmailDto();

        assertThatCode(() -> emailService.sendProposalStatusEmail(dto)).doesNotThrowAnyException();

        verify(mailSenderMock, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void sendProposalStatusEmail_SendsEmailRejected_WhenSuccessful() {

        ProposalStatusEmailDto dto = createProposalStatusEmailDto();

        dto.setStatus("APPROVED");

        assertThatCode(() -> emailService.sendProposalStatusEmail(dto)).doesNotThrowAnyException();

        verify(mailSenderMock, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void sendProposalStatusEmail_ThrowsEmailSendingException_WhenFailed() {

        ProposalStatusEmailDto dto = createProposalStatusEmailDto();

        dto.setUserEmail("");

        assertThatExceptionOfType(EmailSendingException.class)
                .isThrownBy(() -> emailService.sendProposalStatusEmail(dto))
                .withMessageContaining(ERROR_MESSAGE);
    }

    @Test
    void sendProposalReceiptUrl_SendsEmail_WhenSuccessful() {
        ProposalNotificationDto dto = createProposalNotificationDto();

        assertThatCode(() -> emailService.sendProposalReceiptUrl(dto)).doesNotThrowAnyException();

        verify(mailSenderMock, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void sendProposalReceiptUrl_ThrowsEmailSendingException_WhenFailed(){
        ProposalNotificationDto dto = createProposalNotificationDto();

        dto.setUserEmail("");

        assertThatExceptionOfType(EmailSendingException.class)
                .isThrownBy(() -> emailService.sendProposalReceiptUrl(dto))
                .withMessageContaining(ERROR_MESSAGE);
    }

}