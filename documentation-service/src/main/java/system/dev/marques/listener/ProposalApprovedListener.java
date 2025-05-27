package system.dev.marques.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import system.dev.marques.dto.ApprovedProposalDto;
import system.dev.marques.dto.UserReceiptDto;
import system.dev.marques.service.S3Service;

@Service
@RequiredArgsConstructor
public class ProposalApprovedListener {

    private final S3Service s3Service;

    @RabbitListener(queues = "queue.documentation")
    public void listenDocumentQueue(ApprovedProposalDto dto) {
        s3Service.upload(dto);
    }

    @RabbitListener(queues = "queue.user.receipt")
    public void listenDocumentUserQueue(UserReceiptDto dto) {
        s3Service.getProposalUrl(dto);
    }

    @RabbitListener(queues = "queue.user.deletion")
    public void listenDocumentUserQueue(Long id) {
        s3Service.deleteUserFolder(id);
    }
}
