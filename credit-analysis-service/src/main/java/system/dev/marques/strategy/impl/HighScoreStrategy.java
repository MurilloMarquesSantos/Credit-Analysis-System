package system.dev.marques.strategy.impl;

import org.springframework.stereotype.Component;
import system.dev.marques.dto.AnalyzedDto;
import system.dev.marques.enums.ProposalStatus;
import system.dev.marques.strategy.CreditAnalysisStrategy;

@Component
public class HighScoreStrategy implements CreditAnalysisStrategy {
    @Override
    public boolean supports(int score) {
        return score >= 950;
    }

    @Override
    public AnalyzedDto analyse(Long proposalId, Double income, Double requestedAmount, int score,
                               String cpf, int installments) {

        double seventyPercentOfIncome = income * 0.7;

        if (requestedAmount <= seventyPercentOfIncome) {
            return approve(proposalId, cpf);
        }


        double maxMultiplier;
        if (installments >= 50){
            maxMultiplier = 2.6;
        }else{
            maxMultiplier = 1.9;
        }
        double maxValue = income * maxMultiplier;

        if (requestedAmount > maxValue) {
            return reject(proposalId, cpf, score, maxValue);
        }
        return approve(proposalId, cpf);
    }

    private AnalyzedDto reject(Long proposalId, String cpf, int score, double maxValue) {
        return AnalyzedDto.builder()
                .proposalId(proposalId)
                .cpf(cpf)
                .status(ProposalStatus.REJECTED)
                .rejectedReason(String.format(
                        "Your requested amount exceeds the allowed limit of R$ %.2f based on your income and score (%d).",
                        maxValue, score))
                .build();
    }

    private AnalyzedDto approve(Long proposalId, String cpf) {
        return AnalyzedDto.builder()
                .proposalId(proposalId)
                .cpf(cpf)
                .status(ProposalStatus.APPROVED)
                .build();

    }
}
