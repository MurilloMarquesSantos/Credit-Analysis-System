package system.dev.marques.strategy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import system.dev.marques.strategy.CreditAnalysisStrategy;
import system.dev.marques.strategy.CreditStrategyFactory;

import java.util.List;

@Configuration
public class StrategyConfig {

    @Bean
    public CreditStrategyFactory userEnabledStrategyFactory(List<CreditAnalysisStrategy> strategies) {
        return new CreditStrategyFactory(strategies);
    }
}
