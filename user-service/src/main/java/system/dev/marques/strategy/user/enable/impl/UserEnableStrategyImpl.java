package system.dev.marques.strategy.user.enable.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.requests.UserEnableRequest;
import system.dev.marques.mapper.UserMapper;
import system.dev.marques.strategy.user.enable.UserEnableStrategy;

@Component
@RequiredArgsConstructor
public class UserEnableStrategyImpl implements UserEnableStrategy {

    private final UserMapper mapper;

    @Override
    public boolean supports(Object request) {
        return request instanceof UserEnableRequest;
    }

    @Override
    public void updateUser(Object request, User user) {
        mapper.updateUserFromUserEnableRequest((UserEnableRequest) request, user);
    }
}
