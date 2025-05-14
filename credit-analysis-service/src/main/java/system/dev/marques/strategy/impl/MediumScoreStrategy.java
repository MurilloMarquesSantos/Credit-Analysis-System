package system.dev.marques.strategy.impl;

import org.springframework.stereotype.Component;
import system.dev.marques.strategy.CreditAnalysisStrategy;

@Component
public class MediumScoreStrategy implements CreditAnalysisStrategy {
    @Override
    public boolean supports(int score) {
        return score >= 400 && score <= 700;
    }

    @Override
    public String analyse(Double income, Double requestedAmount, int score) {
        Double maxValue = income + (income * 0.2);
        if (requestedAmount.compareTo(maxValue) > 0) {
            return "Value surpassed the maximum!";

        }
        return "APPROVED WITH MEDIUM SCORE!";
    }
}
