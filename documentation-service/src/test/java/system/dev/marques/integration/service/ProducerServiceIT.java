package system.dev.marques.integration.service;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import system.dev.marques.dto.ProposalNotificationDto;
import system.dev.marques.integration.AbstractIntegration;
import system.dev.marques.service.ProducerService;

import static org.assertj.core.api.Assertions.assertThat;
import static system.dev.marques.util.QueueDtoCreator.createProposalNotificationDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=9597")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ProducerServiceIT extends AbstractIntegration {

    @Autowired
    private Queue notificationQueue;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ProducerService producerService;

    @Test
    void shouldSendNotificationToNotificationQueue() {

        ProposalNotificationDto dto = createProposalNotificationDto();

        producerService.sendNotification(dto);

        Object message = rabbitTemplate.receiveAndConvert(notificationQueue.getName(), 5000);

        assertThat(message).isNotNull().isInstanceOf(ProposalNotificationDto.class);

        ProposalNotificationDto received = (ProposalNotificationDto) message;

        assertThat(received.getUserEmail()).isEqualTo(dto.getUserEmail());


    }
}
