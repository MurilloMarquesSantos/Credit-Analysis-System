package system.dev.marques.integrationTests.repository;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import system.dev.marques.domain.Roles;
import system.dev.marques.integrationTests.AbstractIntegration;
import system.dev.marques.repository.RolesRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static system.dev.marques.util.RoleCreator.createRoleAdmin;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RolesRepositoryIT extends AbstractIntegration {

    @Autowired
    private RolesRepository rolesRepository;

    @AfterEach
    void tearDown() {
        rolesRepository.deleteAll();
    }

    @Test
    void save_PersistRole_WhenSuccessful() {

        Roles roleAdmin = createRoleAdmin();

        Roles savedRole = rolesRepository.save(roleAdmin);

        assertThat(savedRole.getId()).isNotNull();

        assertThat(savedRole.getName()).isEqualTo(roleAdmin.getName());
    }

    @Test
    void findByName_ReturnsRolesOptional_WhenSuccessful() {
        Roles roleAdmin = createRoleAdmin();

        rolesRepository.save(roleAdmin);

        Optional<Roles> rolesOpt = rolesRepository.findByName(roleAdmin.getName());

        assertThat(rolesOpt).isPresent();

        assertThat(rolesOpt.get().getName()).isEqualTo(roleAdmin.getName());
    }

    @Test
    void findByName_ReturnsEmptyOptional_WhenRoleIsNotFound() {
        Roles roleAdmin = createRoleAdmin();

        rolesRepository.save(roleAdmin);

        Optional<Roles> rolesOpt = rolesRepository.findByName("");

        assertThat(rolesOpt).isNotPresent();
    }

    @Test
    void existsByName_ReturnsTrue_WhenRoleIsFound() {
        Roles roleAdmin = createRoleAdmin();

        rolesRepository.save(roleAdmin);

        boolean exists = rolesRepository.existsByName("ADMIN");

        assertThat(exists).isTrue();

    }

    @Test
    void existsByName_ReturnsFalse_WhenRoleIsNotFound() {
        Roles roleAdmin = createRoleAdmin();

        rolesRepository.save(roleAdmin);

        boolean exists = rolesRepository.existsByName("USER");

        assertThat(exists).isFalse();

    }
}
