package system.dev.marques.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import system.dev.marques.domain.Proposal;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
}
