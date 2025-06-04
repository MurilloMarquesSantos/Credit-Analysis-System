package system.dev.marques.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import system.dev.marques.util.QueueDtoCreator;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.*;
import static system.dev.marques.util.QueueDtoCreator.createApprovedProposalDto;
import static system.dev.marques.util.QueueDtoCreator.createProposalStatusEmailDto;

@ExtendWith(MockitoExtension.class)
class ProducerServiceTest {

    @InjectMocks
    private ProducerService producerService;

    @Mock
    private RabbitTemplate rabbitTemplateMock;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(producerService, "creditExchangeName", "credit.exchange");
        ReflectionTestUtils.setField(producerService, "notificationExchangeName", "status.exchange");
        ReflectionTestUtils.setField(producerService, "documentationExchangeName", "document.exchange");

        doNothing().when(rabbitTemplateMock).convertAndSend(anyString(), anyString(), any(Object.class));

    }

    @Test
    void sendProposalToCredit_SendsMessage_WhenSuccessful() {
        assertThatCode(() -> producerService.sendProposalToCredit(QueueDtoCreator.createProposalCreditDto())).doesNotThrowAnyException();

        verify(rabbitTemplateMock, times(1))
                .convertAndSend(anyString(), anyString(), any(Object.class));
    }

    @Test
    void sendProposalStatus_SendsMessage_WhenSuccessful() {
        assertThatCode(() -> producerService.sendProposalStatus(createProposalStatusEmailDto())).doesNotThrowAnyException();

        verify(rabbitTemplateMock, times(1))
                .convertAndSend(anyString(), anyString(), any(Object.class));
    }

    @Test
    void sendApprovedProposal_SendsMessage_WhenSuccessful() {
        assertThatCode(() -> producerService.sendApprovedProposal(createApprovedProposalDto())).doesNotThrowAnyException();

        verify(rabbitTemplateMock, times(1))
                .convertAndSend(anyString(), anyString(), any(Object.class));
    }




}