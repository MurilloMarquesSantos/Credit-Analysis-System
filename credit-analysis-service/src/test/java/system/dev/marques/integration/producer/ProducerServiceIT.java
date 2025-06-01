package system.dev.marques.integration.producer;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import system.dev.marques.dto.AnalyzedDto;
import system.dev.marques.integration.AbstractIntegration;
import system.dev.marques.service.ProducerService;

import static org.assertj.core.api.Assertions.assertThat;
import static system.dev.marques.util.QueueDtoCreator.createAnalyzedDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=7778")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ProducerServiceIT extends AbstractIntegration {

    @Autowired
    private ProducerService producerService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue analyzedCreditQueue;

    @Test
    void shouldSendAnalyzedProposalToCorrectQueue() {

        AnalyzedDto dto = createAnalyzedDto();

        producerService.sendAnalyzedProposal(dto);

        Object message = rabbitTemplate.receiveAndConvert(analyzedCreditQueue.getName(), 5000);

        assertThat(message).isNotNull().isInstanceOf(AnalyzedDto.class);

        AnalyzedDto received = (AnalyzedDto) message;

        assertThat(received.getCpf()).isEqualTo(dto.getCpf());
    }
}
