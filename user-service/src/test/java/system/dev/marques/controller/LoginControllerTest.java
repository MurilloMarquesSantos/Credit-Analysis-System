package system.dev.marques.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import system.dev.marques.domain.dto.responses.TokenLoginResponse;
import system.dev.marques.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.when;
import static system.dev.marques.util.TokenResponseCreator.createTokenLoginResponse;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private UserService userServiceMock;

    @Test
    void login_ReturnsLoginView_WhenSuccessful() {
        String view = loginController.login();

        assertThat(view).isEqualTo("login");

    }

    @Test
    void homePage_ReturnsTokenResponse_WhenSuccessful() {

        TokenLoginResponse token = createTokenLoginResponse();

        when(userServiceMock.createToken(ArgumentMatchers.any())).thenReturn(token);

        ResponseEntity<TokenLoginResponse> responseEntity = loginController.homePage(null);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull().isEqualTo(token);
    }

}