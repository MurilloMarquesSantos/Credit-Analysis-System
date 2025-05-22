package system.dev.marques.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.repository.RolesRepository;

import java.util.Set;

@Component
public class UserCreator {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private RolesRepository rolesRepository;

    public User createUser() {
        return User.builder()
                .cpf("12345678909")
                .email("murilloa@gmail.com")
                .name("Murillo Marques")
                .phoneNumber("11885786421")
                .password(encoder.encode("Murillo@12345"))
                .roles(Set.of(rolesRepository.findByName("USER").orElseThrow()))
                .valid(true)
                .build();
    }

    public User createAdmin() {
        return User.builder()
                .cpf("39053344705")
                .email("murillo@gmail.com")
                .name("Murillo Marques")
                .phoneNumber("11885786423")
                .password(encoder.encode("Murillo@12345"))
                .roles(Set.of(rolesRepository.findByName("ADMIN").orElseThrow()))
                .valid(true)
                .build();
    }

    public UserRequest createUserRequest() {
        return UserRequest.builder().email("murillomars@gmail.com")
                .name("Murillo Marques")
                .cpf("11144477735")
                .password("Murillo@1212")
                .phoneNumber("11844786423")
                .build();
    }

    public User createInvalidUser() {
        return User.builder()
                .cpf("01234567890")
                .email("murilloab@gmail.com")
                .name("Murillo Marques")
                .phoneNumber("12855786421")
                .password(encoder.encode("Murillo@12345"))
                .roles(Set.of(rolesRepository.findByName("USER").orElseThrow()))
                .valid(false)
                .build();
    }
}
