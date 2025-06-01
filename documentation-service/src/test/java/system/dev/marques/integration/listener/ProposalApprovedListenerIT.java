package system.dev.marques.integration.listener;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import system.dev.marques.DocumentationApplication;
import system.dev.marques.dto.ApprovedProposalDto;
import system.dev.marques.dto.UserReceiptDto;
import system.dev.marques.integration.AbstractIntegration;
import system.dev.marques.integration.config.MockConfig;
import system.dev.marques.service.S3Service;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static system.dev.marques.util.QueueDtoCreator.createApprovedProposalDto;
import static system.dev.marques.util.QueueDtoCreator.createUserReceiptDto;

@SpringBootTest(classes = {DocumentationApplication.class, MockConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "server.port=9596")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ProposalApprovedListenerIT extends AbstractIntegration {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private S3Service s3Service;

    @Test
    void shouldListenToDocumentQueueAndUploadPDF() {

        ApprovedProposalDto dto = createApprovedProposalDto();

        rabbitTemplate.convertAndSend("", "queue.documentation", dto);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(s3Service, times(1)).upload(dto));
    }

    @Test
    void shouldListenToDocumentUserQueueAndGetProposalURL() {

        UserReceiptDto dto = createUserReceiptDto();

        rabbitTemplate.convertAndSend("", "queue.user.receipt", dto);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(s3Service, times(1)).getProposalUrl(dto));
    }

    @Test
    void shouldListenToUserDeletionQueueAndDeleteFolder() {

        Long userId = 1L;

        rabbitTemplate.convertAndSend("", "queue.user.deletion", userId);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(s3Service, times(1)).deleteUserFolder(userId));
    }
}
