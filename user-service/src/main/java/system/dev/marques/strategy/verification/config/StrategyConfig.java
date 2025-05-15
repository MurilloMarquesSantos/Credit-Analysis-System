package system.dev.marques.strategy.verification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import system.dev.marques.strategy.verification.NewAccountValidationStrategy;
import system.dev.marques.strategy.verification.UserValidationStrategyFactory;

import java.util.List;

@Configuration
public class StrategyConfig {

    @Bean
    public UserValidationStrategyFactory userValidationStrategyFactory(List<NewAccountValidationStrategy> strategies) {
        return new UserValidationStrategyFactory(strategies);
    }
}
