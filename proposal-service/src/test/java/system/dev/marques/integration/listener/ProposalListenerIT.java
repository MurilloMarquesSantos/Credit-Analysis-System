package system.dev.marques.integration.listener;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import system.dev.marques.domain.Proposal;
import system.dev.marques.domain.dto.AnalyzedDto;
import system.dev.marques.domain.dto.ProposalDto;
import system.dev.marques.integration.AbstractIntegration;
import system.dev.marques.repository.ProposalRepository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static system.dev.marques.util.QueueDtoCreator.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=9000")
class ProposalListenerIT extends AbstractIntegration {

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @AfterEach
    void tearDown() {
        proposalRepository.deleteAll();

    }

    @Test
    void shouldListenToQueueAndSaveProposal() {
        ProposalDto dto = createProposalDto();

        rabbitTemplate.convertAndSend("", "queue.proposal", dto);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Optional<Proposal> proposalOpt = proposalRepository.findByUserEmail(dto.getUserEmail());
                    assertThat(proposalOpt).isPresent();
                    assertThat(proposalOpt.get().getId()).isNotNull();
                    assertThat(proposalOpt.get().getUserEmail()).isEqualTo(dto.getUserEmail());
                });

    }

    @Test
    void shouldListenToQueueAndUpdateStatus() {

        AnalyzedDto dto = createAnalyzedDto();

        Proposal proposal = createProposal();

        Proposal savedProposal = proposalRepository.save(proposal);

        dto.setProposalId(savedProposal.getId());

        rabbitTemplate.convertAndSend("", "queue.analyzed.credit", dto);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Optional<Proposal> proposalOpt = proposalRepository.findById(savedProposal.getId());
                    assertThat(proposalOpt).isPresent();

                    assertThat(proposalOpt.get().getStatus().toString()).hasToString("APPROVED");
                });

    }

    @Test
    void shouldListenToQueueDeleteUserId() {

        Proposal proposal = createProposal();

        Proposal savedProposal = proposalRepository.save(proposal);

        rabbitTemplate.convertAndSend("", "queue.proposal.delete", proposal.getUserId());


        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Optional<Proposal> proposalOpt = proposalRepository.findById(savedProposal.getId());

                    assertThat(proposalOpt).isEmpty();
                });
    }
}
