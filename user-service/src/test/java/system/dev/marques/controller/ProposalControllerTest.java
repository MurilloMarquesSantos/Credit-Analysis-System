package system.dev.marques.controller;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import system.dev.marques.domain.dto.proposal.ProposalRequest;
import system.dev.marques.service.ProposalService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.when;

@ExtendWith(MockitoExtension.class)
class ProposalControllerTest {

    @InjectMocks
    private ProposalController proposalController;

    @Mock
    private ProposalService proposalServiceMock;

    @Test
    void proposal_ReturnsStringMessage_WhenSuccessful() throws BadRequestException {

        when(proposalServiceMock.propose(ArgumentMatchers.any(ProposalRequest.class), ArgumentMatchers.any()))
                .thenReturn("Proposal sent for review. Please monitor your email inbox for further updates.");

        ResponseEntity<String> responseEntity = proposalController.proposal(new ProposalRequest(), null);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull()
                .isEqualTo("Proposal sent for review. Please monitor your email inbox for further updates.");
    }

}