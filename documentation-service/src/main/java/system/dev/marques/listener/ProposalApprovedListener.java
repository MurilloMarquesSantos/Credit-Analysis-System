package system.dev.marques.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import system.dev.marques.dto.ApprovedProposalDto;
import system.dev.marques.service.S3Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProposalApprovedListener {

    private final S3Service s3Service;

    @RabbitListener(queues = "queue.documentation")
    public void listenDocumentQueue(ApprovedProposalDto dto) {
        s3Service.upload(dto);
    }
}
