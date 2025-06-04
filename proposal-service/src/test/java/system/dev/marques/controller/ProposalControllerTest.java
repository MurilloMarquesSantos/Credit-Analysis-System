package system.dev.marques.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import system.dev.marques.domain.dto.reponse.ProposalHistoryResponse;
import system.dev.marques.service.ProposalService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.when;
import static system.dev.marques.util.ProposalCreator.createProposalHistoryResponse;

@ExtendWith(MockitoExtension.class)
class ProposalControllerTest {

    @InjectMocks
    private ProposalController controller;

    @Mock
    private ProposalService proposalServiceMock;

    @Test
    void getProposal_ReturnsHistoryList_WhenSuccessful() {

        when(proposalServiceMock.getProposalHistory(anyLong())).thenReturn(List.of(createProposalHistoryResponse()));

        ResponseEntity<List<ProposalHistoryResponse>> responseEntity = controller.getProposalHistory(1L);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull().isNotEmpty().hasSize(1);

        ProposalHistoryResponse response = createProposalHistoryResponse();

        assertThat(responseEntity.getBody().getFirst().getProposalId()).isEqualTo(response.getProposalId());
    }

}