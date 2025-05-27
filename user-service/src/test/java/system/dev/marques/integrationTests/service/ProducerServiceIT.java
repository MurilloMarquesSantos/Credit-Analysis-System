package system.dev.marques.integrationTests.service;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import system.dev.marques.domain.dto.proposal.ProposalUserInfo;
import system.dev.marques.domain.dto.rabbitmq.CreatedUserDto;
import system.dev.marques.domain.dto.rabbitmq.UserReceiptDto;
import system.dev.marques.domain.dto.rabbitmq.ValidUserDto;
import system.dev.marques.integrationTests.AbstractIntegration;
import system.dev.marques.service.ProducerService;

import static org.assertj.core.api.Assertions.assertThat;
import static system.dev.marques.util.QueueDtoCreator.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=8887")
class ProducerServiceIT extends AbstractIntegration {

    @Autowired
    private ProducerService producerService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier("proposalQueue")
    private Queue proposalQueue;

    @Autowired
    @Qualifier("userValidationQueue")
    private Queue userValidationQueue;

    @Autowired
    @Qualifier("userCreatedQueue")
    private Queue userCreatedQueue;

    @Autowired
    @Qualifier("documentationQueue")
    private Queue userReceiptQueue;

    @Test
    void shouldSendProposalToCorrectQueue() {

        ProposalUserInfo dto = createProposalUserInfo();

        producerService.sendProposal(dto);

        Object message = rabbitTemplate.receiveAndConvert(proposalQueue.getName(), 5000);

        assertThat(message).isNotNull().isInstanceOf(ProposalUserInfo.class);

        ProposalUserInfo received = (ProposalUserInfo) message;

        assertThat(received.getCpf()).isEqualTo(dto.getCpf());

        assertThat(received.getUserEmail()).isEqualTo(dto.getUserEmail());

        assertThat(received.getUserName()).isEqualTo(dto.getUserName());
    }

    @Test
    void shouldSendValidationToCorrectQueue() {
        ValidUserDto dto = createValidUserDto();

        producerService.sendValidation(dto);

        Object message = rabbitTemplate.receiveAndConvert(userValidationQueue.getName(), 5000);

        assertThat(message).isNotNull().isInstanceOf(ValidUserDto.class);

        ValidUserDto received = (ValidUserDto) message;

        assertThat(received.getEmail()).isEqualTo(dto.getEmail());
    }

    @Test
    void shouldSendCreatedToCorrectQueue() {
        CreatedUserDto dto = createCreatedUserDto();

        producerService.sendCreated(dto);

        Object message = rabbitTemplate.receiveAndConvert(userCreatedQueue.getName(), 5000);

        assertThat(message).isNotNull().isInstanceOf(CreatedUserDto.class);

        CreatedUserDto received = (CreatedUserDto) message;

        assertThat(received.getEmail()).isEqualTo(dto.getEmail());
    }

    @Test
    void shouldSendUserReceiptToCorrectQueue() {
        UserReceiptDto dto = createUserReceiptDto();

        producerService.sendUserReceipt(dto);

        Object message = rabbitTemplate.receiveAndConvert(userReceiptQueue.getName(), 5000);

        assertThat(message).isNotNull().isInstanceOf(UserReceiptDto.class);

        UserReceiptDto received = (UserReceiptDto) message;

        assertThat(received.getProposalId()).isEqualTo(dto.getProposalId());

        assertThat(received).isEqualTo(dto);

    }
}
