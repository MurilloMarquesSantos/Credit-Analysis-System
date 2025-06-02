package system.dev.marques.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import system.dev.marques.domain.dto.responses.UserAdminResponse;
import system.dev.marques.service.AdminService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.doNothing;
import static system.dev.marques.util.UserCreatorStatic.createUserAdminResponse;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminService adminServiceMock;

    @Test
    void list_ReturnsPageOfUsers_WhenSuccessful() {

        PageImpl<UserAdminResponse> userAdminPage = new PageImpl<>(List.of(createUserAdminResponse()));

        when(adminServiceMock.findAll(any())).thenReturn(userAdminPage);

        ResponseEntity<Page<UserAdminResponse>> responseEntity = adminController.list(null);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getTotalElements()).isEqualTo(1);

    }

    @Test
    void getByUserId_ReturnsUser_WhenSuccessful() {

        UserAdminResponse userAdminResponse = createUserAdminResponse();

        when(adminServiceMock.findById(anyLong())).thenReturn(userAdminResponse);

        ResponseEntity<UserAdminResponse> responseEntity = adminController.getUserById(1L);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isEqualTo(userAdminResponse);
    }

    @Test
    void deleteUser_DoesNotThrowException_WhenSuccessful() {

        doNothing().when(adminServiceMock).deleteUser(anyLong());

        assertThatCode(() -> adminController.deleteUser(1L)).doesNotThrowAnyException();

    }

}