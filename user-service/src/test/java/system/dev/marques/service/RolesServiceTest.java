package system.dev.marques.service;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import system.dev.marques.domain.Roles;
import system.dev.marques.repository.RolesRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.when;
import static system.dev.marques.util.RoleCreator.createRoleAdmin;

@ExtendWith(MockitoExtension.class)
class RolesServiceTest {

    @InjectMocks
    private RolesService rolesService;

    @Mock
    private RolesRepository rolesRepositoryMock;

    @Test
    void getRoleByName_ReturnsRole_WhenSuccessful() throws BadRequestException {

        Roles roleAdmin = createRoleAdmin();
        roleAdmin.setId(1L);

        when(rolesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(roleAdmin));

        Roles response = rolesService.getRoleByName("");

        assertThat(response).isNotNull().isEqualTo(roleAdmin);
    }

    @Test
    void getRoleByName_ThrowsBadRequestException_WhenRoleNotExist() {

        when(rolesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> rolesService.getRoleByName(""))
                .withMessageContaining("This role does not exist");

    }


}