package system.dev.marques.integrationTests.service;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import system.dev.marques.domain.dto.proposal.ProposalUserInfo;
import system.dev.marques.domain.dto.rabbitmq.*;
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

    @Autowired
    @Qualifier("proposalDeleteQueue")
    private Queue proposalDeletionQueue;

    @Autowired
    @Qualifier("userDeleteQueue")
    private Queue userDeletionQueue;

    @Autowired
    @Qualifier("userConfirmationQueue")
    private Queue userConfirmationQueue;

    @Autowired
    @Qualifier("documentationDeletionQueue")
    private Queue documentationDeletionQueue;


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

    @Test
    void shouldSendProposalDeleteToCorrectQueue() {

        producerService.sendProposalDeletion(1L);

        Object message = rabbitTemplate.receiveAndConvert(proposalDeletionQueue.getName(), 5000);

        assertThat(message).isNotNull();

        Long id = (Long) message;

        assertThat(id).isNotNull();
    }

    @Test
    void shouldSendDocumentDeleteToCorrectQueue() {

        producerService.sendDocumentDeletion(1L);

        Object message = rabbitTemplate.receiveAndConvert(documentationDeletionQueue.getName(), 5000);

        assertThat(message).isNotNull();

        Long id = (Long) message;

        assertThat(id).isNotNull();
    }

    @Test
    void shouldSendDeleteDtoToCorrectQueue() {

        DeleteUserDto dto = createDeleteUserDto();

        producerService.sendDeleteDto(dto);

        Object message = rabbitTemplate.receiveAndConvert(userDeletionQueue.getName(), 5000);

        assertThat(message).isNotNull();

        DeleteUserDto received = (DeleteUserDto) message;

        assertThat(received).isEqualTo(dto);
    }

    @Test
    void shouldSendDeleteUserConfirmationToCorrectQueue() {

        DeleteUserConfirmationDto dto = createDeleteUserConfirmationDto();

        producerService.sendUserDeleteConfirmation(dto);

        Object message = rabbitTemplate.receiveAndConvert(userConfirmationQueue.getName(), 5000);

        assertThat(message).isNotNull();

        DeleteUserConfirmationDto received = (DeleteUserConfirmationDto) message;

        assertThat(received).isEqualTo(dto);
    }
}
