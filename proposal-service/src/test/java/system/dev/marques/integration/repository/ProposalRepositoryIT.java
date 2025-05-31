package system.dev.marques.integration.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import system.dev.marques.domain.Proposal;
import system.dev.marques.integration.AbstractIntegration;
import system.dev.marques.repository.ProposalRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static system.dev.marques.util.ProposalCreator.createProposal;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProposalRepositoryIT extends AbstractIntegration {

    @Autowired
    private ProposalRepository proposalRepository;

    @AfterEach
    void tearDown() {
        proposalRepository.deleteAll();
    }

    @Test
    void save_PersistsProposal_WhenSuccessful() {
        Proposal proposal = createProposal();

        Proposal savedProposal = proposalRepository.save(proposal);

        assertThat(savedProposal.getId()).isNotNull();

        assertThat(savedProposal.getCpf()).isEqualTo(proposal.getCpf());
    }

    @Test
    void findByUserId_ReturnsProposalList_WhenSuccessful() {

        Proposal proposal = createProposal();

        proposalRepository.save(proposal);

        List<Proposal> proposalList = proposalRepository.findByUserId(proposal.getUserId());

        assertThat(proposalList).isNotEmpty().isNotNull().hasSize(1);

        assertThat(proposalList.getFirst().getUserId()).isEqualTo(proposal.getUserId());
    }

    @Test
    void findById_ReturnsProposalOptional_WhenSuccessful() {

        Proposal proposal = createProposal();

        Proposal savedProposal = proposalRepository.save(proposal);

        Optional<Proposal> proposalOpt = proposalRepository.findById(savedProposal.getId());

        assertThat(proposalOpt).isPresent();

        assertThat(proposalOpt.get().getId()).isEqualTo(savedProposal.getId());

    }

    @Test
    void findById_ReturnsEmptyProposalOptional_WhenProposalIsNotFound() {

        Optional<Proposal> proposalOpt = proposalRepository.findById(1L);

        assertThat(proposalOpt).isNotPresent();

    }

    @Test
    void findByUserEmail_ReturnsProposalOptional_WhenSuccessful() {

        Proposal proposal = createProposal();

        proposalRepository.save(proposal);

        Optional<Proposal> proposalOpt = proposalRepository.findByUserEmail(proposal.getUserEmail());

        assertThat(proposalOpt).isPresent();

        assertThat(proposalOpt.get().getUserId()).isEqualTo(proposal.getUserId());

    }


    @Test
    void findByUserEmail_ReturnsEmptyProposalOptional_WhenProposalIsNotFound() {

        Optional<Proposal> proposalOpt = proposalRepository.findByUserEmail("");

        assertThat(proposalOpt).isNotPresent();

    }
}
