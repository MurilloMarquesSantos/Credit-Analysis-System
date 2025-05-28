package system.dev.marques.integrationTests.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import system.dev.marques.domain.Roles;
import system.dev.marques.domain.User;
import system.dev.marques.integrationTests.AbstractIntegration;
import system.dev.marques.repository.RolesRepository;
import system.dev.marques.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryIT extends AbstractIntegration {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    private static User user;

    @BeforeEach
    void setUp() {

        if (!rolesRepository.existsByName("USER")) {
            rolesRepository.save(new Roles(null, "USER"));
        }

        user = User.builder()
                .cpf("12345678909")
                .email("murilloa@gmail.com")
                .name("Murillo Marques")
                .phoneNumber("123")
                .password("123")
                .roles(Set.of(rolesRepository.findByName("USER").orElseThrow()))
                .valid(true)
                .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void save_PersistsUser_WhenSuccessful() {

        User savedUser = userRepository.save(user);

        assertThat(user.getName()).isEqualTo(savedUser.getName());
    }

    @Test
    void findUserByEmail_ReturnsUserOptional_WhenSuccessful() {

        User savedUser = userRepository.save(user);

        Optional<User> userOpt = userRepository.findUserByEmail(savedUser.getEmail());

        assertThat(userOpt).isPresent();

        assertThat(userOpt.get().getName()).isEqualTo(savedUser.getName());

    }

    @Test
    void findUserByEmail_ReturnsEmptyOptional_WhenThereIsNoUserWithGivenEmail() {

        userRepository.save(user);

        Optional<User> userOpt = userRepository.findUserByEmail("");

        assertThat(userOpt).isNotPresent();

    }

    @Test
    void findAllByRoleName_ReturnsListOfUser_WhenSuccessful() {

        User savedUser = userRepository.save(user);

        List<User> userList = userRepository.findAllByRoleName("USER");

        assertThat(userList).isNotEmpty().isNotNull().hasSize(1);

        assertThat(userList.getFirst().getName()).isEqualTo(savedUser.getName());
    }

    @Test
    void findAllByRoleName_ReturnsEmptyList_WhenThereIsNoUserWithRole() {

        userRepository.save(user);

        List<User> userList = userRepository.findAllByRoleName("");

        assertThat(userList).isEmpty();

    }

    @Test
    void findAll_ReturnsListOfUser_WhenSuccessful() {

        User savedUser = userRepository.save(user);

        List<User> userList = userRepository.findAll();

        assertThat(userList).isNotEmpty().isNotNull().hasSize(1);

        assertThat(userList.getFirst().getName()).isEqualTo(savedUser.getName());

    }

    @Test
    void findById_ReturnsUserOptional_WhenSuccessful() {

        User savedUser = userRepository.save(user);

        Optional<User> userOpt = userRepository.findById(savedUser.getId());

        assertThat(userOpt).isPresent();

        assertThat(userOpt.get().getName()).isEqualTo(savedUser.getName());

    }

    @Test
    void findById_ReturnsEmptyOptional_WhenThereIsNoUserWithGivenId() {

        userRepository.save(user);

        Optional<User> userOpt = userRepository.findById(10L);

        assertThat(userOpt).isNotPresent();

    }

    @Test
    void deleteById_RemovesUser_WhenSuccessful() {

        User savedUser = userRepository.save(user);

        userRepository.deleteById(savedUser.getId());

        Optional<User> userOpt = userRepository.findById(savedUser.getId());

        assertThat(userOpt).isNotPresent();
    }


}
