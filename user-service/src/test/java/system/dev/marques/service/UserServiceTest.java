package system.dev.marques.service;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import system.dev.marques.domain.Roles;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.rabbitmq.CreatedUserDto;
import system.dev.marques.domain.dto.rabbitmq.DeleteUserDto;
import system.dev.marques.domain.dto.rabbitmq.UserReceiptDto;
import system.dev.marques.domain.dto.rabbitmq.ValidUserDto;
import system.dev.marques.domain.dto.requests.UserEnableRequest;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.domain.dto.requests.UserRequestGoogle;
import system.dev.marques.domain.dto.responses.ProposalHistoryResponse;
import system.dev.marques.domain.dto.responses.TokenLoginResponse;
import system.dev.marques.domain.dto.responses.UserEnabledResponse;
import system.dev.marques.domain.dto.responses.UserResponse;
import system.dev.marques.exception.custom.InvalidTokenException;
import system.dev.marques.exception.custom.ServiceUnavailableException;
import system.dev.marques.mapper.UserMapper;
import system.dev.marques.repository.UserRepository;
import system.dev.marques.strategy.enable.UserEnableStrategy;
import system.dev.marques.strategy.enable.UserEnabledStrategyFactory;
import system.dev.marques.strategy.verification.UserValidationStrategyFactory;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.when;
import static system.dev.marques.util.FormCreator.createDeleteForm;
import static system.dev.marques.util.QueueDtoCreator.createDeleteUserDto;
import static system.dev.marques.util.QueueDtoCreator.createUserReceiptDto;
import static system.dev.marques.util.TokenResponseCreator.createTokenLoginResponse;
import static system.dev.marques.util.UserCreatorStatic.*;

@SuppressWarnings("rawtypes")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserValidationStrategyFactory validationFactoryMock;

    @Mock
    private UserEnabledStrategyFactory enabledStrategyFactoryMock;

    @Mock
    private TokenService tokenServiceMock;

    @Mock
    private ProducerService producerServiceMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @Mock
    private RolesService rolesServiceMock;

    @Mock
    private UserMapper userMapperMock;

    @Mock
    private UserEnableStrategy userEnableStrategyMock;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Test
    void saveUser_ReturnsSavedUser_WhenSuccessful() throws BadRequestException {
        doNothing().when(validationFactoryMock).validate(any(UserRequest.class));
        doNothing().when(producerServiceMock).sendCreated(ArgumentMatchers.any(CreatedUserDto.class));
        when(tokenServiceMock.generateEnableUserToken(any())).thenReturn("token");
        when(userRepositoryMock.save(any(User.class))).thenReturn(createSavedUser());
        when(passwordEncoderMock.encode(anyString())).thenReturn("encodedPassword");
        when(rolesServiceMock.getRoleByName(anyString())).thenReturn(new Roles(2L, "USER"));
        when(userMapperMock.toUser(any(UserRequest.class))).thenReturn(createUser());
        when(userMapperMock.toUserResponse(any(User.class))).thenReturn(createUserResponse());

        UserRequest request = createUserRequest();

        UserResponse userResponse = userService.saveUser(request, "x");

        assertThat(userResponse).isNotNull();

        assertThat(userResponse.getId()).isNotNull();

        assertThat(userResponse.getName()).isEqualTo(request.getName());

    }

    @Test
    void saveUserWithGoogleSource_ReturnsSavedUser_WhenSuccessful() throws BadRequestException {
        doNothing().when(producerServiceMock).sendCreated(ArgumentMatchers.any(CreatedUserDto.class));
        when(tokenServiceMock.generateEnableUserToken(any())).thenReturn("token");
        when(userRepositoryMock.save(any(User.class))).thenReturn(createSavedUser());
        when(passwordEncoderMock.encode(anyString())).thenReturn("encodedPassword");
        when(rolesServiceMock.getRoleByName(anyString())).thenReturn(new Roles(2L, "USER"));
        when(userMapperMock.toUser(any(UserRequest.class))).thenReturn(createUser());
        when(userMapperMock.toUserResponse(any(User.class))).thenReturn(createUserResponse());

        UserRequest request = createUserRequest();

        UserResponse userResponse = userService.saveUser(request, "google");

        assertThat(userResponse).isNotNull();

        assertThat(userResponse.getId()).isNotNull();

        assertThat(userResponse.getName()).isEqualTo(request.getName());

    }

    @Test
    void saveAdmin_ReturnsSavedAdmin_WhenSuccessful() throws BadRequestException {
        doNothing().when(validationFactoryMock).validate(any(UserRequest.class));
        when(userRepositoryMock.save(any(User.class))).thenReturn(createSavedUser());
        when(passwordEncoderMock.encode(anyString())).thenReturn("encodedPassword");
        when(rolesServiceMock.getRoleByName(anyString())).thenReturn(new Roles(1L, "ADMIN"));
        when(userMapperMock.toUser(any(UserRequest.class))).thenReturn(createUser());
        when(userMapperMock.toUserResponse(any(User.class))).thenReturn(createUserResponse());

        UserRequest request = createUserRequest();

        UserResponse userResponse = userService.saveAdmin(request);

        assertThat(userResponse).isNotNull();

        assertThat(userResponse.getId()).isNotNull();

        assertThat(userResponse.getName()).isEqualTo(request.getName());

    }

    @Test
    void findByUserId_ReturnsUser_WhenSuccessful() {
        when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.of(createUser()));

        User foundUser = userService.findUserById(1L);

        assertThat(foundUser).isNotNull();

    }


    @Test
    void findByUserId_ThrowsIllegalArgumentException_WhenNotFound() {
        when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userService.findUserById(1L))
                .withMessageContaining("User not found");

    }


    @Test
    void enableUserFromGoogle_ReturnsEnabledUser_WhenSuccessful() {
        when(enabledStrategyFactoryMock.getStrategy(any())).thenReturn(userEnableStrategyMock);
        doNothing().when(userEnableStrategyMock).updateUser(any(), any(User.class));
        when(tokenServiceMock.validateToken(anyString(), anyLong())).thenReturn(true);
        when(userRepositoryMock.save(any(User.class))).thenReturn(createUser());
        when(userMapperMock.toUserEnabledResponse(any(User.class))).thenReturn(createUserEnabledResponse());
        when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.of(createUser()));

        UserRequestGoogle request = createUserRequestGoogle();

        UserEnabledResponse response = userService.enableUserFromGoogle("", request, () -> "1");

        assertThat(response).isNotNull();

        assertThat(response.getName()).isEqualTo(request.getName());

    }

    @Test
    void enableUserFromGoogle_ThrowsInvalidTokenException_WhenTokenIsInvalid() {
        when(tokenServiceMock.validateToken(anyString(), anyLong())).thenReturn(false);

        assertThatExceptionOfType(InvalidTokenException.class)
                .isThrownBy(() -> userService.enableUserFromGoogle("", null, () -> "1"))
                .withMessageContaining("Link has expired or is no longer valid!");
    }

    @Test
    void enableUser_ReturnsEnabledUser_WhenSuccessful() {
        when(enabledStrategyFactoryMock.getStrategy(any())).thenReturn(userEnableStrategyMock);
        doNothing().when(userEnableStrategyMock).updateUser(any(), any(User.class));
        when(tokenServiceMock.validateToken(anyString(), anyLong())).thenReturn(true);
        when(userRepositoryMock.save(any(User.class))).thenReturn(createUser());
        when(userMapperMock.toUserEnabledResponse(any(User.class))).thenReturn(createUserEnabledResponse());
        when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.of(createUser()));

        UserEnableRequest request = createUserEnableRequest();

        UserEnabledResponse response = userService.enableUser("", request, () -> "1");

        assertThat(response).isNotNull();

        assertThat(response.getIncome()).isEqualTo(request.getIncome());

    }

    @Test
    void enableUser_ThrowsInvalidTokenException_WhenTokenIsInvalid() {
        when(tokenServiceMock.validateToken(anyString(), anyLong())).thenReturn(false);

        assertThatExceptionOfType(InvalidTokenException.class)
                .isThrownBy(() -> userService.enableUser("", null, () -> "1"))
                .withMessageContaining("Link has expired or is no longer valid!");
    }

    @Test
    void notifyUser_SendsNotificationGoogle_WhenSuccessful() {

        User savedUser = createSavedUser();

        when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.of(createSavedUser()));

        when(passwordEncoderMock.matches(anyString(), anyString())).thenReturn(true);

        doNothing().when(producerServiceMock).sendValidation(any(ValidUserDto.class));


        Jwt token = new Jwt(
                "tokenValue",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                Map.of("alg", "none"),
                Map.of("sub", String.valueOf(savedUser.getId()))
        );

        assertThatCode(() -> userService.notifyUser(token)).doesNotThrowAnyException();

    }

    @Test
    void notifyUser_SendsNotificationForm_WhenSuccessful() {

        User savedUser = createSavedUser();

        when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.of(createSavedUser()));
        when(passwordEncoderMock.matches(anyString(), anyString())).thenReturn(false);
        doNothing().when(producerServiceMock).sendValidation(any(ValidUserDto.class));


        Jwt token = new Jwt(
                "tokenValue",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                Map.of("alg", "none"),
                Map.of("sub", String.valueOf(savedUser.getId()))
        );

        assertThatCode(() -> userService.notifyUser(token)).doesNotThrowAnyException();

    }

    @Test
    void createToken_ReturnsTokenResponse_WhenSuccessful() {
        when(userRepositoryMock.findUserByEmail(anyString())).thenReturn(Optional.of(createUser()));
        when(tokenServiceMock.generateToken(any(User.class))).thenReturn(createTokenLoginResponse());

        TokenLoginResponse response = userService.createToken(() -> "");

        assertThat(response).isNotNull();

        assertThat(response.getExpiresIn()).isEqualTo(300L);
    }

    @Test
    void createToken_ThrowsIllegalArgumentException_WhenEmailIsNotFound() {
        when(userRepositoryMock.findUserByEmail(anyString())).thenReturn(Optional.empty());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.createToken(() -> ""))
                .withMessageContaining("User not found");

    }

    @Test
    void findByEmail_ReturnsUser_WhenSuccessful() {
        User user = createUser();
        when(userRepositoryMock.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        Optional<User> userOpt = userService.findByEmail("");

        assertThat(userOpt).isPresent().isNotNull();

        assertThat(userOpt.get().getEmail()).isEqualTo(user.getEmail());
    }


    @Test
    @SuppressWarnings("unchecked")
    void fetchHistory_ReturnPagedHistory_WhenSuccessful() {

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/service/history/{id}", 1L)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ProposalHistoryResponse.class))
                .thenReturn(Flux.fromIterable(List.of(createProposalHistoryResponse())));

        Page<ProposalHistoryResponse> response = userService.
                fetchHistory(() -> "1", PageRequest.of(0, 10));

        assertThat(response).isNotNull();

        assertThat(response.getTotalElements()).isEqualTo(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void fetchHistory_ReturnsEmptyPage_WhenHistoryIsEmpty() {

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/service/history/{id}", 1L)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ProposalHistoryResponse.class))
                .thenReturn(Flux.fromIterable(List.of()));

        Page<ProposalHistoryResponse> response = userService.
                fetchHistory(() -> "1", PageRequest.of(0, 10));

        assertThat(response).isNotNull().isEmpty();

    }

    @Test
    void fallback_ThrowsServiceUnavailableException_WhenServiceIsNotAvailable() {
        Throwable thrown = catchThrowable(() -> userService
                .fallback(() -> "1", PageRequest.of(0, 10), new RuntimeException("simulated")));

        assertThat(thrown)
                .isInstanceOf(ServiceUnavailableException.class)
                .hasMessage("Service is currently unavailable. Please try again later.");
    }

    @Test
    void sendUserReceipt_SendsEmail_WhenSuccessful() {
        doNothing().when(producerServiceMock).sendUserReceipt(any(UserReceiptDto.class));
        when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.of(createSavedUser()));
        when(userMapperMock.toUserReceiptDto(any(User.class))).thenReturn(createUserReceiptDto());

        String response = userService.sendUserReceipt(1L, () -> "1");

        assertThat(response).isNotBlank()
                .isEqualTo("Request processed successfully, stay alert on your email box.");
    }

    @Test
    void submitDeleteRequest_SendsEmail_WhenSuccessful() {
        doNothing().when(producerServiceMock).sendDeleteDto(any(DeleteUserDto.class));
        when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.of(createSavedUser()));
        when(userRepositoryMock.findAllByRoleName(anyString())).thenReturn(List.of(createSavedUser()));
        when(userMapperMock.toDeleteUserDto(any(User.class))).thenReturn(createDeleteUserDto());

        String response = userService.submitDeleteRequest(createDeleteForm(), () -> "1");

        assertThat(response).isNotBlank()
                .isEqualTo("Request processed successfully, stay alert on your email box.");
    }

}