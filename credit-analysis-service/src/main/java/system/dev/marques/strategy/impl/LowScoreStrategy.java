package system.dev.marques.strategy.impl;

import org.springframework.stereotype.Component;
import system.dev.marques.strategy.CreditAnalysisStrategy;

@Component
public class LowScoreStrategy implements CreditAnalysisStrategy {
    @Override
    public boolean supports(int score) {
        return score < 400;
    }

    @Override
    public String analyse(Double income, Double requestedAmount, int score) {
        return "Score too low!";
    }
}
