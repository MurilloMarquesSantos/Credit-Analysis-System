package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.*;
import system.dev.marques.mapper.UserMapper;
import system.dev.marques.patterns.enabling.UserEnableStrategy;
import system.dev.marques.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final List<UserEnableStrategy> strategies;

    private final UserMapper mapper;

    public UserResponse save(UserRequest request) {
        User user = mapper.toUser(request);
        User savedUser = repository.save(user);
        return mapper.toUserResponse(savedUser);
    }

    public UserEnabledResponse enableUserFromGoogle(UserRequestGoogle request, Long id) throws Exception {
        return enableUserInternal(request, id);
    }

    public UserEnabledResponse enableUser(UserEnableRequest request, Long id) throws Exception {
        return enableUserInternal(request, id);
    }

    // todo customize Exception, Use encoder in the password, and use Principal instead of Long to get the id
    private UserEnabledResponse enableUserInternal(Object request, Long id) throws Exception {
        User user = repository.findById(id)
                .orElseThrow(Exception::new);

        UserEnableStrategy strategy = strategies.stream()
                .filter(s -> s.supports(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No strategy found for request: " + request));

        strategy.updateUser(request, user);

//        user.setPassword(encode);

        user.setEnabled(true);

        User savedUser = repository.save(user);

        return mapper.toUserEnabledResponse(savedUser);
    }


}
