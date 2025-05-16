package system.dev.marques.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import system.dev.marques.domain.Client;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByClientId(String clientId);
}
