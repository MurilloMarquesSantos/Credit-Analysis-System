package system.dev.marques.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import system.dev.marques.dto.AnalyzedDto;
import system.dev.marques.dto.ProposalCreditDto;
import system.dev.marques.service.CreditAnalysisService;
import system.dev.marques.service.ProducerService;

@Service
@RequiredArgsConstructor
public class CreditListener {

    private final CreditAnalysisService creditAnalysisService;

    private final ProducerService producerService;

    @RabbitListener(queues = "queue.credit.analysis")
    public void listenCreditProposal(ProposalCreditDto dto) {
        AnalyzedDto result = creditAnalysisService.analyzeCredit(dto);
        producerService.sendAnalyzedProposal(result);
    }
}
