package system.dev.marques.strategy.enable.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.requests.UserEnableRequest;
import system.dev.marques.mapper.UserMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static system.dev.marques.util.UserCreatorStatic.*;

@ExtendWith(MockitoExtension.class)
class UserEnableStrategyImplTest {

    @InjectMocks
    private UserEnableStrategyImpl strategy;

    @Mock
    private UserMapper mapperMock;


    @Test
    void supports_ReturnsTrue_WhenSuccessful() {

        boolean supports = strategy.supports(createUserEnableRequest());

        assertThat(supports).isTrue();
    }

    @Test
    void supports_ReturnsFalse_WhenIsNotInstance() {

        boolean supports = strategy.supports(createUserRequestGoogle());

        assertThat(supports).isFalse();
    }

    @Test
    void updateUser_UpdatesUser_WhenSuccessful() {

        doNothing().when(mapperMock).updateUserFromUserEnableRequest(any(UserEnableRequest.class), any(User.class));

        UserEnableRequest request = createUserEnableRequest();

        User savedUser = createSavedUser();

        assertThatCode(() -> strategy.updateUser(request, savedUser))
                .doesNotThrowAnyException();

    }

}