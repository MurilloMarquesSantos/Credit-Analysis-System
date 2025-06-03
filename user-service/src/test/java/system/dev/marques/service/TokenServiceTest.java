package system.dev.marques.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import system.dev.marques.domain.EnableUserToken;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.responses.TokenLoginResponse;
import system.dev.marques.repository.TokenRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.doNothing;
import static system.dev.marques.util.EnableTokenCreator.createUserToken;
import static system.dev.marques.util.UserCreatorStatic.createUser;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private TokenRepository tokenRepositoryMock;

    @Mock
    private JwtEncoder jwtEncoderMock;

    @Test
    void generateToken_ReturnsTokenResponseWhenSuccessful() {
        Jwt token = getJwt();

        User user = createUser();

        when(jwtEncoderMock.encode(any())).thenReturn(token);

        TokenLoginResponse response = tokenService.generateToken(user);

        assertThat(response).isNotNull();

        assertThat(response.getExpiresIn()).isEqualTo(600);

    }


    @Test
    void generateEnableUserToken_ReturnsTokenString_WhenSuccessful() {

        User user = createUser();

        String response = tokenService.generateEnableUserToken(user);

        assertThat(response).isNotNull().isNotEmpty().hasSize(36);

    }


    @Test
    void validateToken_ReturnsTrue_WhenSuccessful() {
        EnableUserToken userToken = createUserToken();

        when(tokenRepositoryMock.findByToken(anyString())).thenReturn(Optional.of(userToken));
        doNothing().when(tokenRepositoryMock).deleteById(anyLong());

        boolean response = tokenService.validateToken("", 1L);

        assertThat(response).isTrue();

    }

    @Test
    void validateToken_ReturnsFalse_WhenOptionalIsEmpty() {

        when(tokenRepositoryMock.findByToken(anyString())).thenReturn(Optional.empty());

        boolean response = tokenService.validateToken("", 1L);

        assertThat(response).isFalse();

    }

    @Test
    void validateToken_ReturnsFalse_WhenUserIdIsDifferent() {
        EnableUserToken userToken = createUserToken();

        when(tokenRepositoryMock.findByToken(anyString())).thenReturn(Optional.of(userToken));

        boolean response = tokenService.validateToken("", 2L);

        assertThat(response).isFalse();

    }

    @Test
    void validateToken_ReturnsFalse_WhenTokenIsExpired() {
        EnableUserToken userToken = createUserToken();

        userToken.setExpirationDate(LocalDateTime.now());

        when(tokenRepositoryMock.findByToken(anyString())).thenReturn(Optional.of(userToken));

        boolean response = tokenService.validateToken("", 1L);

        assertThat(response).isFalse();

    }

    private static Jwt getJwt() {
        return new Jwt(
                "TEST_TOKEN",
                Instant.now(),
                Instant.now().plusSeconds(600L),
                Map.of("alg", "none"),
                Map.of("scope", "user")
        );
    }

}