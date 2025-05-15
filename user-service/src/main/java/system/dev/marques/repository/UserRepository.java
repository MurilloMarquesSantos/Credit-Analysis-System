package system.dev.marques.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import system.dev.marques.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);
}
