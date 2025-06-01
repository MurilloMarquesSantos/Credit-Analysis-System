package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import system.dev.marques.dto.AnalyzedDto;
import system.dev.marques.dto.ProposalCreditDto;
import system.dev.marques.strategy.CreditAnalysisStrategy;
import system.dev.marques.strategy.CreditStrategyFactory;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class CreditAnalysisService {

    private final CreditStrategyFactory strategyFactory;

    private final Random random = new Random();

    public AnalyzedDto analyzeCredit(ProposalCreditDto dto) {

        int score = random.nextInt(350, 1001);

        CreditAnalysisStrategy strategy = strategyFactory.getStrategy(score);

        return strategy.analyse(dto.getProposalId(), dto.getUserIncome(), dto.getRequestedAmount(), score, dto.getCpf(),
                dto.getInstallments());

    }
}
