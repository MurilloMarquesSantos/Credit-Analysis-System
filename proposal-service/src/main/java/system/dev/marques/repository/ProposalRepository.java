package system.dev.marques.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import system.dev.marques.domain.Proposal;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

    List<Proposal> findByUserId(Long userId);
}
