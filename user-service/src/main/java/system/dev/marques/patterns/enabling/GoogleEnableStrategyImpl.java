package system.dev.marques.patterns.enabling;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.UserRequestGoogle;
import system.dev.marques.mapper.UserMapper;

@Component
@RequiredArgsConstructor
public class GoogleEnableStrategyImpl implements UserEnableStrategy {

    private final UserMapper mapper;



    @Override
    public boolean supports(Object request) {
        return request instanceof UserRequestGoogle;
    }

    @Override
    public void updateUser(Object request, User user) {
        mapper.updateUserFromGoogleRequest((UserRequestGoogle) request, user);
    }
}
