package system.dev.marques.strategy.enable.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.requests.UserRequestGoogle;
import system.dev.marques.mapper.UserMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.*;
import static system.dev.marques.util.UserCreatorStatic.*;

@ExtendWith(MockitoExtension.class)
class GoogleEnableStrategyImplTest {

    @InjectMocks
    private GoogleEnableStrategyImpl strategy;

    @Mock
    private UserMapper mapperMock;

    @Mock
    private BCryptPasswordEncoder encoderMock;

    @Test
    void supports_ReturnsTrue_WhenSuccessful() {

        boolean supports = strategy.supports(createUserRequestGoogle());

        assertThat(supports).isTrue();
    }

    @Test
    void supports_ReturnsFalse_WhenIsNotInstance() {

        boolean supports = strategy.supports(createUserEnableRequest());

        assertThat(supports).isFalse();
    }

    @Test
    void updateUser_UpdatesUser_WhenSuccessful() {

        doNothing().when(mapperMock).updateUserFromGoogleRequest(any(UserRequestGoogle.class), any(User.class));

        when(encoderMock.encode(any(String.class))).thenReturn("encodedPassword");

        UserRequestGoogle request = createUserRequestGoogle();

        User savedUser = createSavedUser();

        assertThatCode(() -> strategy.updateUser(request, savedUser))
                .doesNotThrowAnyException();

    }


}