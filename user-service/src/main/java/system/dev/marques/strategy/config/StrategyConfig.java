package system.dev.marques.strategy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import system.dev.marques.strategy.enable.UserEnableStrategy;
import system.dev.marques.strategy.enable.UserEnabledStrategyFactory;
import system.dev.marques.strategy.verification.NewAccountValidationStrategy;
import system.dev.marques.strategy.verification.UserValidationStrategyFactory;

import java.util.List;

@Configuration
public class StrategyConfig {

    @Bean
    public UserEnabledStrategyFactory userEnabledStrategyFactory(List<UserEnableStrategy> strategies) {
        return new UserEnabledStrategyFactory(strategies);
    }

    @Bean
    public UserValidationStrategyFactory userValidationStrategyFactory(List<NewAccountValidationStrategy> strategies) {
        return new UserValidationStrategyFactory(strategies);
    }
}
