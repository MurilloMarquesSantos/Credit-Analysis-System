package system.dev.marques.strategy.impl;

import org.springframework.stereotype.Component;
import system.dev.marques.dto.AnalyzedDto;
import system.dev.marques.enums.ProposalStatus;
import system.dev.marques.strategy.CreditAnalysisStrategy;

@Component
public class LowScoreStrategy implements CreditAnalysisStrategy {
    @Override
    public boolean supports(int score) {
        return score < 400;
    }

    @Override
    public AnalyzedDto analyse(Long proposalId, Double income, Double requestedAmount, int score, String cpf) {
        return AnalyzedDto.builder()
                .proposalId(proposalId)
                .cpf(cpf)
                .status(ProposalStatus.REJECTED)
                .rejectedReason("Your score is to low do submit a proposal: " + score)
                .build();
    }
}
