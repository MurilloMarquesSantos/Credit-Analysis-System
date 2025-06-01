package system.dev.marques.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import system.dev.marques.domain.dto.requests.UserEnableRequest;
import system.dev.marques.domain.dto.requests.UserRequestGoogle;
import system.dev.marques.domain.dto.responses.UserEnabledResponse;
import system.dev.marques.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.when;
import static system.dev.marques.util.UserCreatorStatic.*;

@ExtendWith(SpringExtension.class)
class ValidateUserControllerTest {

    @InjectMocks
    private ValidateUserController validateController;

    @Mock
    private UserService userServiceMock;

    @BeforeEach
    void setUp() {

        when(userServiceMock.enableUserFromGoogle(ArgumentMatchers.anyString(),
                ArgumentMatchers.any(UserRequestGoogle.class), ArgumentMatchers.any()))
                .thenReturn(createUserEnabledResponse());


        when(userServiceMock.enableUser(ArgumentMatchers.anyString(),
                ArgumentMatchers.any(UserEnableRequest.class), ArgumentMatchers.any()))
                .thenReturn(createUserEnabledResponse());
    }

    @Test
    void enableUserGoogle_ReturnsUserEnabledResponse_WhenSuccessful() {

        UserRequestGoogle request = createUserRequestGoogle();

        ResponseEntity<UserEnabledResponse> responseEntity =
                validateController.enableUserGoogle("", request, null);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(responseEntity.getBody()).isNotNull();


        UserEnabledResponse response = responseEntity.getBody();

        assertThat(response.getIncome()).isEqualTo(request.getIncome());

    }


    @Test
    void enableUserForm_ReturnsUserEnabledResponse_WhenSuccessful() {

        UserEnableRequest request = createUserEnableRequest();

        ResponseEntity<UserEnabledResponse> responseEntity =
                validateController.enableUserForm("", request, null);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(responseEntity.getBody()).isNotNull();

        UserEnabledResponse response = responseEntity.getBody();

        assertThat(response.getIncome()).isEqualTo(request.getIncome());

    }

}