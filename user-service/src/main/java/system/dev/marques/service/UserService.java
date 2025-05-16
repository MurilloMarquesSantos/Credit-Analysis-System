package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.rabbitmq.CreatedUserDto;
import system.dev.marques.domain.dto.rabbitmq.ValidUserDto;
import system.dev.marques.domain.dto.requests.UserEnableRequest;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.domain.dto.requests.UserRequestGoogle;
import system.dev.marques.domain.dto.responses.TokenLoginResponse;
import system.dev.marques.domain.dto.responses.UserEnabledResponse;
import system.dev.marques.domain.dto.responses.UserResponse;
import system.dev.marques.exception.InvalidTokenException;
import system.dev.marques.mapper.UserMapper;
import system.dev.marques.repository.UserRepository;
import system.dev.marques.strategy.enable.UserEnableStrategy;
import system.dev.marques.strategy.enable.UserEnabledStrategyFactory;
import system.dev.marques.strategy.verification.UserValidationStrategyFactory;

import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    @Value("${google.default.password}")
    private String googlePassword;

    private final UserRepository repository;

    private final UserEnabledStrategyFactory enableStrategyFactory;

    private final UserValidationStrategyFactory validationStrategyFactory;

    private final UserMapper mapper;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    private final RolesService rolesService;

    private final ProducerService producerService;

    private static final String GOOGLE_SOURCE = "google";


    public UserResponse saveUser(UserRequest request, String source) throws BadRequestException {
        if (!source.equals(GOOGLE_SOURCE)){
            validateRequest(request);
        }
        User user = mapper.toUser(request);
        prepareUserDefault(user);
        User savedUser = repository.save(user);
        sendCreatedMessage(savedUser, source);
        return mapper.toUserResponse(savedUser);
    }

    public UserResponse saveAdmin(UserRequest request) throws BadRequestException {
        validateRequest(request);
        User user = mapper.toUser(request);
        prepareUserAdmin(user);
        User savedUser = repository.save(user);
        return mapper.toUserResponse(savedUser);
    }

    public User findUserById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }


    public UserEnabledResponse enableUserFromGoogle(String token, UserRequestGoogle request, Principal principal) {
        if (isTokenValid(token, principal)) {
            return enableUserInternal(request, principal);
        }
        throw new InvalidTokenException("This link is expired!");
    }

    public UserEnabledResponse enableUser(String token, UserEnableRequest request, Principal principal) {
        if (isTokenValid(token, principal)) {
            return enableUserInternal(request, principal);
        }
        throw new InvalidTokenException("This link is expired!");
    }

    public void notifyUser(Jwt token) {
        Long userId = Long.valueOf(token.getSubject());
        User user = findUserById(userId);
        String password = user.getPassword();
        if (passwordEncoder.matches(googlePassword, password)) {
            producerService.sendValidation(formatUser(user, GOOGLE_SOURCE));
        } else {
            producerService.sendValidation(formatUser(user, "formlogin"));
        }
    }

    private UserEnabledResponse enableUserInternal(Object request, Principal principal) {

        Long userId = Long.valueOf(principal.getName());

        User user = findUserById(userId);

        UserEnableStrategy strategy = enableStrategyFactory.getStrategy(request);

        strategy.updateUser(request, user);

        user.setValid(true);

        User savedUser = repository.save(user);

        return mapper.toUserEnabledResponse(savedUser);
    }

    public TokenLoginResponse createToken(Principal principal) {
        User user = findUserByEmailOrThrowIllegalArgumentException(principal.getName());

        return tokenService.generateToken(user);
    }

    private void prepareUserAdmin(User user) throws BadRequestException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(rolesService.getRoleByName("ADMIN")));
        user.setValid(true);
    }

    private void prepareUserDefault(User user) throws BadRequestException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(rolesService.getRoleByName("USER")));
    }

    public Optional<User> findByEmail(String email) {
        return repository.findUserByEmail(email);
    }

    public User findUserByEmailOrThrowIllegalArgumentException(String email) {
        return repository.findUserByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private ValidUserDto formatUser(User user, String source) {
        ValidUserDto dto = ValidUserDto.builder()
                .email(user.getEmail())
                .source(source).build();
        String token = tokenService.generateEnableUserToken(user);
        setUrl(source, dto, token);
        return dto;
    }

    private static void setUrl(String source, ValidUserDto dto, String token) {
        if (source.equals(GOOGLE_SOURCE)) {
            dto.setUrl("http://localhost:8080/validate/google?token=" + token);
        } else {
            dto.setUrl("http://localhost:8080/validate/form-login?token=" + token);
        }
    }

    private static void setUrl(String source, CreatedUserDto dto, String token) {
        if (source.equals(GOOGLE_SOURCE)) {
            dto.setUrl("http://localhost:8080/validate/google?token=" + token);
        } else {
            dto.setUrl("http://localhost:8080/validate/form-login?token=" + token);
        }
    }


    private boolean isTokenValid(String token, Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        return tokenService.validateToken(token, userId);
    }

    private void sendCreatedMessage(User user, String source) {
        CreatedUserDto dto = CreatedUserDto.builder().email(user.getEmail()).name(user.getName()).build();
        String token = tokenService.generateEnableUserToken(user);
        setUrl(source, dto, token);
        producerService.sendCreated(dto);
    }

    private void validateRequest(UserRequest request) throws BadRequestException {
        validationStrategyFactory.validate(request);
    }
}
