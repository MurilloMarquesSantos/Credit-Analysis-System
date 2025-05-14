package system.dev.marques.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import system.dev.marques.dto.ProposalCreditDto;

@Service
@Log4j2
public class CreditListener {

    @RabbitListener(queues = "queue.credit.analysis")
    public void listenCreditProposal(ProposalCreditDto dto) {
        log.info("Received credit proposal: " + dto);
    }
}
