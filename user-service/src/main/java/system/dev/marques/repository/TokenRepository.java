package system.dev.marques.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import system.dev.marques.domain.EnableUserToken;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<EnableUserToken, Long> {

    Optional<EnableUserToken> findByToken(String token);
}
