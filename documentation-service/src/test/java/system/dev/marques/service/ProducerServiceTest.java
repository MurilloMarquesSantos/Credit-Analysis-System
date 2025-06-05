package system.dev.marques.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import system.dev.marques.dto.ProposalNotificationDto;
import system.dev.marques.util.QueueDtoCreator;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ProducerServiceTest {

    @InjectMocks
    private ProducerService producerService;

    @Mock
    private RabbitTemplate rabbitTemplateMock;


    @BeforeEach
    void setUp() {
        ReflectionTestUtils
                .setField(producerService, "notificationExchangeName", "notification.receipt.exchange");
    }

    @Test
    void sendNotification_SendsNotification_WhenSuccessful() {

        doNothing().when(rabbitTemplateMock)
                .convertAndSend(anyString(), anyString(), any(ProposalNotificationDto.class));

        ProposalNotificationDto dto = QueueDtoCreator.createProposalNotificationDto();

        assertThatCode(() -> producerService.sendNotification(dto)).doesNotThrowAnyException();

        verify(rabbitTemplateMock, times(1))
                .convertAndSend(anyString(), anyString(), any(ProposalNotificationDto.class));
    }

}