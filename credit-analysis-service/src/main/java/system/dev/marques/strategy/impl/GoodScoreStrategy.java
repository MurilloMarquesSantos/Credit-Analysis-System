package system.dev.marques.strategy.impl;

import org.springframework.stereotype.Component;
import system.dev.marques.strategy.CreditAnalysisStrategy;

@Component

public class GoodScoreStrategy implements CreditAnalysisStrategy {
    @Override
    public boolean supports(int score) {
        return score >= 700 && score <= 950;
    }

    @Override
    public String analyse(Double income, Double requestedAmount, int score) {
        Double maxValue = income + (income * 0.7);
        if (requestedAmount.compareTo(maxValue) > 0) {
            return "Value surpassed the maximum!";

        }
        return "APPROVED WITH GOOD SCORE!";
    }
}
