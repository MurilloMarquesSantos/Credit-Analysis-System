package system.dev.marques.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.rabbitmq.DeleteUserConfirmationDto;
import system.dev.marques.domain.dto.responses.UserAdminResponse;
import system.dev.marques.mapper.UserMapper;
import system.dev.marques.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.doNothing;
import static system.dev.marques.util.QueueDtoCreator.createDeleteUserConfirmationDto;
import static system.dev.marques.util.UserCreatorStatic.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private ProducerService producerServiceMock;

    @Mock
    private UserMapper mapperMock;

    @Test
    void findAll_ReturnsPageOfUsers_WhenSuccessful() {
        User user = createUser();
        PageImpl<User> pageResponse = new PageImpl<>(List.of(user));
        when(userRepositoryMock.findAll(any(PageRequest.class))).thenReturn(pageResponse);
        when(mapperMock.toUserAdminResponse(any(User.class))).thenReturn(createUserAdminResponse());

        Page<UserAdminResponse> response = adminService.findAll(PageRequest.of(0, 10));

        assertThat(response).isNotNull();

        assertThat(response.getTotalElements()).isEqualTo(1);

        assertThat(response.getContent().getFirst().getName()).isEqualTo(user.getName());
    }

    @Test
    void findById_ReturnsUserAdminResponse_WhenSuccessful() {
        User user = createUser();
        when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.of(user));
        when(mapperMock.toUserAdminResponse(any(User.class))).thenReturn(createUserAdminResponse());


        UserAdminResponse response = adminService.findById(1L);

        assertThat(response).isNotNull();

        assertThat(response.getName()).isEqualTo(user.getName());

    }

    @Test
    void findById_ThrowsIllegalArgumentException_WhenUserNotFound() {
        when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> adminService.findById(1L))
                .withMessageContaining("User not found");
    }

    @Test
    void deleteUser_RemovesUser_WhenSuccessful() {
        doNothing().when(userRepositoryMock).deleteById(anyLong());
        doNothing().when(producerServiceMock).sendUserDeleteConfirmation(any(DeleteUserConfirmationDto.class));
        doNothing().when(producerServiceMock).sendDocumentDeletion(anyLong());
        doNothing().when(producerServiceMock).sendProposalDeletion(anyLong());
        when(mapperMock.toDeleUserConfirmationDto(any(User.class))).thenReturn(createDeleteUserConfirmationDto());
        when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.of(createSavedUser()));

        assertThatCode(() -> adminService.deleteUser(1L)).doesNotThrowAnyException();
    }

    @Test
    void deleteUser_ThrowsIllegalArgumentException_WhenUserNotFound() {
        when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> adminService.deleteUser(1L))
                .withMessageContaining("This user does not exists or have been already deleted");
    }


}