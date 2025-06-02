package system.dev.marques.controller;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import system.dev.marques.domain.dto.requests.DeleteForm;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.domain.dto.responses.ProposalHistoryResponse;
import system.dev.marques.domain.dto.responses.UserResponse;
import system.dev.marques.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.when;
import static system.dev.marques.util.FormCreator.createDeleteForm;
import static system.dev.marques.util.UserCreatorStatic.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userServiceMock;

    @Test
    void create_ReturnsUserResponse_WhenSuccessful() throws BadRequestException {

        when(userServiceMock.saveUser(ArgumentMatchers.any(UserRequest.class), ArgumentMatchers.anyString()))
                .thenReturn(createUserResponse());

        UserRequest request = createUserRequest();

        ResponseEntity<UserResponse> responseEntity = userController.create(request);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getId()).isNotNull();

    }

    @Test
    void createAdmin_ReturnsUserResponse_WhenSuccessful() throws BadRequestException {

        when(userServiceMock.saveAdmin(ArgumentMatchers.any(UserRequest.class)))
                .thenReturn(createUserResponse());

        UserRequest request = createUserRequest();

        ResponseEntity<UserResponse> responseEntity = userController.createAdmin(request);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getId()).isNotNull();
    }

    @Test
    void create_ThrowsException_WhenRequestIsInvalid() throws BadRequestException {

        when(userServiceMock.saveUser(ArgumentMatchers.any(UserRequest.class), ArgumentMatchers.anyString()))
                .thenThrow(new BadRequestException("Validation failed for field(s)"));

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userController.create(createInvalidUserRequest()))
                .withMessageContaining("Validation failed for field(s)");

    }

    @Test
    void getUserHistory_ReturnsHistoryPage_WhenSuccessful() {

        PageImpl<ProposalHistoryResponse> historyPage = new PageImpl<>(List.of(createProposalHistoryResponse()));
        when(userServiceMock.fetchHistory(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(historyPage);


        ResponseEntity<Page<ProposalHistoryResponse>> responseEntity =
                userController.getUserHistory(null, null);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getTotalElements()).isEqualTo(1);

    }

    @Test
    void getUserProposalReceipt_ReturnsStringMessage_WhenSuccessful() {
        when(userServiceMock.sendUserReceipt(ArgumentMatchers.anyLong(), ArgumentMatchers.any()))
                .thenReturn("Request processed successfully, stay alert on your email box.");

        ResponseEntity<String> responseEntity = userController.getUserProposalReceipt(1L, null);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull().isEqualTo
                ("Request processed successfully, stay alert on your email box.");

    }

    @Test
    void userDeleteForm_ReturnsStringMessage_WhenSuccessful() {

        when(userServiceMock.submitDeleteRequest(ArgumentMatchers.any(DeleteForm.class), ArgumentMatchers.any()))
                .thenReturn("Request processed successfully, stay alert on your email box.");

        DeleteForm deleteForm = createDeleteForm();

        ResponseEntity<String> responseEntity = userController.userDeleteForm(deleteForm, null);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull()
                .isEqualTo("Request processed successfully, stay alert on your email box.");

    }

}