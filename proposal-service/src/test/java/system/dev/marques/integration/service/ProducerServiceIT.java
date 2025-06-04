package system.dev.marques.integration.service;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import system.dev.marques.domain.dto.ApprovedProposalDto;
import system.dev.marques.domain.dto.ProposalCreditDto;
import system.dev.marques.domain.dto.ProposalStatusEmailDto;
import system.dev.marques.integration.AbstractIntegration;
import system.dev.marques.service.ProducerService;

import static org.assertj.core.api.Assertions.assertThat;
import static system.dev.marques.util.QueueDtoCreator.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=8889")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ProducerServiceIT extends AbstractIntegration {

    @Autowired
    private ProducerService producerService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier("documentationQueueProposal")
    private Queue documentQueue;

    @Autowired
    @Qualifier("creditQueueProposal")
    private Queue creditQueue;

    @Autowired
    @Qualifier("notificationQueueProposal")
    private Queue notificationStatusQueue;


    @Test
    void shouldSendProposalToCreditQueue() {
        ProposalCreditDto dto = createProposalCreditDto();

        producerService.sendProposalToCredit(dto);

        Object message = rabbitTemplate.receiveAndConvert(creditQueue.getName(), 5000);

        assertThat(message).isNotNull().isInstanceOf(ProposalCreditDto.class);

        ProposalCreditDto received = (ProposalCreditDto) message;

        assertThat(received.getProposalId()).isEqualTo(dto.getProposalId());

    }

    @Test
    void shouldSendProposalStatusToNotificationQueue() {
        ProposalStatusEmailDto emailDto = createProposalStatusEmailDto();

        producerService.sendProposalStatus(emailDto);

        Object message = rabbitTemplate.receiveAndConvert(notificationStatusQueue.getName(), 5000);

        assertThat(message).isNotNull().isInstanceOf(ProposalStatusEmailDto.class);

        ProposalStatusEmailDto received = (ProposalStatusEmailDto) message;

        assertThat(received.getUserEmail()).isEqualTo(emailDto.getUserEmail());


    }

    @Test
    void shouldSendApprovedProposalToDocumentQueue() {
        ApprovedProposalDto dto = createApprovedProposalDto();

        producerService.sendApprovedProposal(dto);

        Object message = rabbitTemplate.receiveAndConvert(documentQueue.getName(), 5000);

        assertThat(message).isNotNull().isInstanceOf(ApprovedProposalDto.class);

        ApprovedProposalDto received = (ApprovedProposalDto) message;

        assertThat(received.getUserEmail()).isEqualTo(dto.getUserEmail());

        assertThat(received.getUserId()).isEqualTo(dto.getUserId());

    }

}
