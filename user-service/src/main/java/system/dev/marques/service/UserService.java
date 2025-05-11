package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.requests.UserEnableRequest;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.domain.dto.requests.UserRequestGoogle;
import system.dev.marques.domain.dto.responses.TokenLoginResponse;
import system.dev.marques.domain.dto.responses.UserEnabledResponse;
import system.dev.marques.domain.dto.responses.UserResponse;
import system.dev.marques.mapper.UserMapper;
import system.dev.marques.repository.UserRepository;
import system.dev.marques.strategy.user.enable.UserEnableStrategy;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository repository;

    private final List<UserEnableStrategy> strategies;

    private final UserMapper mapper;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    private final RolesService rolesService;

    public UserResponse saveUser(UserRequest request) {
        User user = mapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = repository.save(user);
        return mapper.toUserResponse(savedUser);
    }

    public UserResponse saveAdmin(UserRequest request) throws BadRequestException {
        User user = mapper.toUser(request);
        prepareUserAdmin(user);
        User savedUser = repository.save(user);
        return mapper.toUserResponse(savedUser);
    }

    public User findUserById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }


    public UserEnabledResponse enableUserFromGoogle(UserRequestGoogle request, Long id) throws Exception {
        return enableUserInternal(request, id);
    }

    public UserEnabledResponse enableUser(UserEnableRequest request, Long id) throws Exception {
        return enableUserInternal(request, id);
    }
    //todo create the queue that will send the url to notification service containing the url to use either enableUserFromGoogle
    //todo or enableUser
    //todo to clarify,the request from both methods will be on the response body in the controller that will be created lately
    //todo make the password safer
    public void notifyUser(Jwt token) {
        Long userId = Long.valueOf(token.getSubject());
        User user = findUserById(userId);
        String password = user.getPassword();
        if (passwordEncoder.matches("123", password)) {
            log.info("User logged via google");
            // send email with link to method "enableUserFromGoogle"
        }else{
            log.info("User logged via email");
            //send email with link to method enableUser
        }
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

        user.setValid(true);

        User savedUser = repository.save(user);

        return mapper.toUserEnabledResponse(savedUser);
    }

    public TokenLoginResponse createToken(Principal principal) {
        User user = repository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return tokenService.generateToken(user);
    }

    private void prepareUserAdmin(User user) throws BadRequestException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(rolesService.getRoleByName("ADMIN")));
        user.setValid(true);
    }

    public Optional<User> findByEmail(String email) {
        return repository.findUserByEmail(email);
    }


}
