package system.dev.marques.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static system.dev.marques.util.QueueDtoCreator.*;

@ExtendWith(MockitoExtension.class)
class ProducerServiceTest {

    @InjectMocks
    private ProducerService producerService;

    @Mock
    private RabbitTemplate rabbitTemplateMock;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(producerService, "notificationExchange", "notification.exchange");
        ReflectionTestUtils.setField(producerService, "proposalExchange", "proposal.exchange");
        ReflectionTestUtils.setField(producerService, "documentationExchange", "documentation.exchange");

        doNothing().when(rabbitTemplateMock).convertAndSend(anyString(), anyString(), any(Object.class));
    }

    @Test
    void sendValidation_SendsMessage_WhenSuccessful() {
        assertThatCode(() -> producerService.sendValidation(createValidUserDto())).doesNotThrowAnyException();

        verify(rabbitTemplateMock, times(1))
                .convertAndSend(anyString(), anyString(), any(Object.class));
    }

    @Test
    void sendCreated_SendsMessage_WhenSuccessful() {
        assertThatCode(() -> producerService.sendCreated(createCreatedUserDto())).doesNotThrowAnyException();

        verify(rabbitTemplateMock, times(1))
                .convertAndSend(anyString(), anyString(), any(Object.class));
    }

    @Test
    void sendProposal_SendsMessage_WhenSuccessful() {
        assertThatCode(() -> producerService.sendProposal(createProposalUserInfo())).doesNotThrowAnyException();

        verify(rabbitTemplateMock, times(1))
                .convertAndSend(anyString(), anyString(), any(Object.class));
    }

    @Test
    void sendUserReceipt_SendsMessage_WhenSuccessful() {
        assertThatCode(() -> producerService.sendUserReceipt(createUserReceiptDto())).doesNotThrowAnyException();

        verify(rabbitTemplateMock, times(1))
                .convertAndSend(anyString(), anyString(), any(Object.class));
    }

    @Test
    void sendProposalDeletion_SendsMessage_WhenSuccessful() {
        assertThatCode(() -> producerService.sendProposalDeletion(1L)).doesNotThrowAnyException();

        verify(rabbitTemplateMock, times(1))
                .convertAndSend(anyString(), anyString(), any(Object.class));
    }

    @Test
    void sendDocumentDeletion_SendsMessage_WhenSuccessful() {
        assertThatCode(() -> producerService.sendDocumentDeletion(1L)).doesNotThrowAnyException();

        verify(rabbitTemplateMock, times(1))
                .convertAndSend(anyString(), anyString(), any(Object.class));
    }

    @Test
    void sendDeleteDto_SendsMessage_WhenSuccessful() {
        assertThatCode(() -> producerService.sendDeleteDto(createDeleteUserDto())).doesNotThrowAnyException();

        verify(rabbitTemplateMock, times(1))
                .convertAndSend(anyString(), anyString(), any(Object.class));
    }

    @Test
    void sendUserDeleteConfirmation_SendsMessage_WhenSuccessful() {
        assertThatCode(() -> producerService.sendUserDeleteConfirmation(createDeleteUserConfirmationDto()))
                .doesNotThrowAnyException();

        verify(rabbitTemplateMock, times(1))
                .convertAndSend(anyString(), anyString(), any(Object.class));
    }



}