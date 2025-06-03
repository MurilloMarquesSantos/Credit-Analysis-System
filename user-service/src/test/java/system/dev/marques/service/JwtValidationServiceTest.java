package system.dev.marques.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.doNothing;

@ExtendWith(MockitoExtension.class)
class JwtValidationServiceTest {

    @InjectMocks
    private JwtValidationService jwtValidationService;

    @Mock
    private UserService userServiceMock;

    @Test
    void isJwtValid_ReturnsTrue_WhenSuccessful() {

        Jwt token = getJwt();

        boolean response = jwtValidationService.isJwtValid(token);

        assertThat(response).isTrue();

    }

    @Test
    void isJwtValid_ReturnsFalseAndNotifyUser_WhenUserIsInvalid() {

        Jwt token = getInvalidJwt();

        doNothing().when(userServiceMock).notifyUser(any());

        boolean response = jwtValidationService.isJwtValid(token);

        assertThat(response).isFalse();

    }

    private static Jwt getJwt() {
        return new Jwt(
                "tokenValue",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                Map.of("alg", "none"),
                Map.of("valid", true)
        );
    }

    private static Jwt getInvalidJwt() {
        return new Jwt(
                "tokenValue",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                Map.of("alg", "none"),
                Map.of("valid", false)
        );
    }


}