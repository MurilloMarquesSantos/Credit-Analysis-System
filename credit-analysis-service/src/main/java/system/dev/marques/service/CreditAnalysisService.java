package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import system.dev.marques.dto.ProposalCreditDto;
import system.dev.marques.strategy.CreditAnalysisStrategy;
import system.dev.marques.strategy.CreditStrategyFactory;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Log4j2
public class CreditAnalysisService {

    private final CreditStrategyFactory strategyFactory;

    private final Random random = new Random();

    public String analyzeCredit(ProposalCreditDto dto) {

        int score = random.nextInt(1001);

        CreditAnalysisStrategy strategy = strategyFactory.getStrategy(score);

        return strategy.analyse(dto.getUserIncome(), dto.getRequestedAmount(), score);

    }
}
