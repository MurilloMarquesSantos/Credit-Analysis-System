package system.dev.marques.strategy;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CreditStrategyFactory {

    private final List<CreditAnalysisStrategy> strategies;

    public CreditAnalysisStrategy getStrategy(int score) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(score))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("None strategy found for score: " + score));
    }
}
