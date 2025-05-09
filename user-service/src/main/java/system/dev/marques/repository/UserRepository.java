package system.dev.marques.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import system.dev.marques.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
