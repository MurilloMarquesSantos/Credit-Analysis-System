package system.dev.marques.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import system.dev.marques.dto.ProposalCreditDto;
import system.dev.marques.service.CreditAnalysisService;

@Service
@Log4j2
@RequiredArgsConstructor
public class CreditListener {
    private final CreditAnalysisService creditAnalysisService;

    @RabbitListener(queues = "queue.credit.analysis")
    public void listenCreditProposal(ProposalCreditDto dto) {
        log.info("Received credit proposal: " + dto);
        String result = creditAnalysisService.analyzeCredit(dto);
        log.info(result);
    }
}
