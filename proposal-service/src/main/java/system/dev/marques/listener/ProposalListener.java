package system.dev.marques.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.dto.ProposalDto;

@Service
@Log4j2
public class ProposalListener {

    @RabbitListener(queues = "queue.proposal")
    public void listenValidationQueue(ProposalDto dto) {
        log.info(dto.toString());
    }

}
