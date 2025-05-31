package system.dev.marques.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import system.dev.marques.domain.Proposal;

import java.util.List;
import java.util.Optional;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

    List<Proposal> findByUserId(Long userId);

    Optional<Proposal> findByUserEmail(String userEmail);
}
