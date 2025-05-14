package system.dev.marques.strategy.enable.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.requests.UserRequestGoogle;
import system.dev.marques.mapper.UserMapper;
import system.dev.marques.strategy.enable.UserEnableStrategy;

@Component
@RequiredArgsConstructor
public class GoogleEnableStrategyImpl implements UserEnableStrategy {

    private final UserMapper mapper;

    private final BCryptPasswordEncoder encoder;

    @Override
    public boolean supports(Object request) {
        return request instanceof UserRequestGoogle;
    }

    @Override
    public void updateUser(Object request, User user) {
        UserRequestGoogle googleRequest = (UserRequestGoogle) request;
        mapper.updateUserFromGoogleRequest(googleRequest, user);
        user.setPassword(encoder.encode(googleRequest.getPassword()));
    }
}
