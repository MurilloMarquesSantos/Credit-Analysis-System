package system.dev.marques.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import system.dev.marques.domain.Roles;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByName(String name);
}
