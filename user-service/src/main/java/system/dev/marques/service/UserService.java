package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.ValidUserDto;
import system.dev.marques.domain.dto.requests.UserEnableRequest;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.domain.dto.requests.UserRequestGoogle;
import system.dev.marques.domain.dto.responses.TokenLoginResponse;
import system.dev.marques.domain.dto.responses.TokenResponse;
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

    private final ProducerService producerService;


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


    public UserEnabledResponse enableUserFromGoogle(String token, UserRequestGoogle request, Principal principal)
            throws Exception {
        if (isTokenValid(token, principal)) {
            return enableUserInternal(request, principal);
        }
        throw new BadRequestException("Invalid token");
    }

    public UserEnabledResponse enableUser(String token, UserEnableRequest request, Principal principal)
            throws Exception {
        if (isTokenValid(token, principal)) {
            return enableUserInternal(request, principal);
        }
        throw new BadRequestException("Invalid token");
    }

    public void notifyUser(Jwt token) {
        Long userId = Long.valueOf(token.getSubject());
        User user = findUserById(userId);
        String password = user.getPassword();
        if (passwordEncoder.matches("123", password)) {
            producerService.enviar(formatUser(user, "google"));
        } else {
            log.info("User logged via email");
            producerService.enviar(formatUser(user, "formlogin"));
        }
    }

    private UserEnabledResponse enableUserInternal(Object request, Principal principal) {

        Long userId = Long.valueOf(principal.getName());

        User user = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

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

    private ValidUserDto formatUser(User user, String source) {
        ValidUserDto dto = ValidUserDto.builder()
                .email(user.getEmail())
                .source(source).build();
        String token = tokenService.generateEnableUserToken(user);
        if (source.equals("google")) {
            dto.setUrl("http://localhost:8080/validate/google?token=" + token);
        } else {
            dto.setUrl("http://localhost:8080/validate/form-login?token=" + token);
        }
        return dto;
    }

    private boolean isTokenValid(String token, Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        return tokenService.validateToken(token, userId);
    }
}
