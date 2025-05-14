package system.dev.marques.strategy.enable;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserEnabledStrategyFactory {

    private final List<UserEnableStrategy> strategies;

    public UserEnableStrategy getStrategy(Object request) {
        return strategies.stream()
                .filter(s -> s.supports(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No strategy found for request: " + request));
    }


}
