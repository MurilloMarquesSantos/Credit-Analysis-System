package system.dev.marques.strategy.impl;

import org.springframework.stereotype.Component;
import system.dev.marques.dto.AnalyzedDto;
import system.dev.marques.enums.ProposalStatus;
import system.dev.marques.strategy.CreditAnalysisStrategy;

@Component

public class GoodScoreStrategy implements CreditAnalysisStrategy {
    @Override
    public boolean supports(int score) {
        return score >= 700 && score <= 950;
    }

    @Override
    public AnalyzedDto analyse(Long proposalId, Double income, Double requestedAmount, int score, String cpf) {
        Double maxValue = income + (income * 0.7);
        if (requestedAmount.compareTo(maxValue) > 0) {
            return AnalyzedDto.builder()
                    .proposalId(proposalId)
                    .cpf(cpf)
                    .status(ProposalStatus.REJECTED)
                    .rejectedReason("Your requested amount exceeds the allowed limit," +
                            " which is 170% of your income based on your credit score. Current score:" + score)
                    .build();
        }
        return AnalyzedDto.builder()
                .proposalId(proposalId)
                .cpf(cpf)
                .status(ProposalStatus.APPROVED)
                .build();
    }
}
