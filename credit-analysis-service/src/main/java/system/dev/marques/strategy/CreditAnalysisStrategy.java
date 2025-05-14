package system.dev.marques.strategy;

import system.dev.marques.dto.AnalyzedDto;

public interface CreditAnalysisStrategy {
    boolean supports(int score);

    AnalyzedDto analyse(Long proposalId, Double income, Double requestedAmount, int score, String cpf, int installments);
}
