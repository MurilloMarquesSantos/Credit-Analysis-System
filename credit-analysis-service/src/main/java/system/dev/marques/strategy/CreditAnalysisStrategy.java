package system.dev.marques.strategy;

public interface CreditAnalysisStrategy {
    boolean supports(int score);

    String analyse(Double income, Double requestedAmount, int score);
}
