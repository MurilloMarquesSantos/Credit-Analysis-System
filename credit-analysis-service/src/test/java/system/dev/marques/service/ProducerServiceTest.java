package system.dev.marques.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import system.dev.marques.dto.AnalyzedDto;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.*;
import static system.dev.marques.util.QueueDtoCreator.createAnalyzedDto;

@ExtendWith(MockitoExtension.class)
class ProducerServiceTest {

    @InjectMocks
    private ProducerService producerService;

    @Mock
    private RabbitTemplate rabbitTemplateMock;

    @Test
    void sendAnalyzedProposal_SendsAnalyzedProposal_WhenSuccessful() {
        ReflectionTestUtils.setField(producerService, "exchangeName", "exchange.name");
        doNothing().when(rabbitTemplateMock).convertAndSend(anyString(), anyString(), any(AnalyzedDto.class));
        AnalyzedDto dto = createAnalyzedDto();

        assertThatCode(() -> producerService.sendAnalyzedProposal(dto)).doesNotThrowAnyException();

        verify(rabbitTemplateMock, times(1))
                .convertAndSend(anyString(), anyString(), any(AnalyzedDto.class));

    }

}