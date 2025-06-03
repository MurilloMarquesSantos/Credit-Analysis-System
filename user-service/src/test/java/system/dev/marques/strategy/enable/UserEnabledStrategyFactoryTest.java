package system.dev.marques.strategy.enable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import system.dev.marques.domain.dto.requests.UserEnableRequest;
import system.dev.marques.domain.dto.requests.UserRequestGoogle;
import system.dev.marques.strategy.enable.impl.GoogleEnableStrategyImpl;
import system.dev.marques.strategy.enable.impl.UserEnableStrategyImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.when;
import static system.dev.marques.util.UserCreatorStatic.createUserEnableRequest;
import static system.dev.marques.util.UserCreatorStatic.createUserRequestGoogle;

@ExtendWith(MockitoExtension.class)
class UserEnabledStrategyFactoryTest {

    @InjectMocks
    private UserEnabledStrategyFactory factory;

    @Mock
    private GoogleEnableStrategyImpl googleStrategyMock;

    @Mock
    private UserEnableStrategyImpl userStrategyMock;

    @BeforeEach
    void setUp() {
        factory = new UserEnabledStrategyFactory(List.of(googleStrategyMock, userStrategyMock));
    }

    @Test
    void getStrategy_ReturnsGoogleStrategy_WhenSuccessful() {

        when(googleStrategyMock.supports(any(UserRequestGoogle.class))).thenReturn(true);

        UserRequestGoogle request = createUserRequestGoogle();

        UserEnableStrategy strategy = factory.getStrategy(request);

        assertThat(strategy).isInstanceOf(GoogleEnableStrategyImpl.class);

    }

    @Test
    void getStrategy_ReturnsUserStrategy_WhenSuccessful() {

        when(userStrategyMock.supports(any(UserEnableRequest.class))).thenReturn(true);

        UserEnableRequest request = createUserEnableRequest();

        UserEnableStrategy strategy = factory.getStrategy(request);

        assertThat(strategy).isInstanceOf(UserEnableStrategyImpl.class);

    }

    @Test
    void getStrategy_ThrowsIllegalArgumentException_WhenNoStrategyIsFound() {

        when(userStrategyMock.supports(any(UserEnableRequest.class))).thenReturn(false);

        UserEnableRequest request = createUserEnableRequest();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> factory.getStrategy(request))
                .withMessageContaining("No strategy found for request: " + request);

    }

    @Test
    void getStrategyGoogle_ThrowsIllegalArgumentException_WhenNoStrategyIsFound() {

        when(googleStrategyMock.supports(any(UserRequestGoogle.class))).thenReturn(false);

        UserRequestGoogle request = createUserRequestGoogle();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> factory.getStrategy(request))
                .withMessageContaining("No strategy found for request: " + request);

    }

}