package system.dev.marques.listener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import system.dev.marques.domain.Proposal;
import system.dev.marques.domain.dto.AnalyzedDto;
import system.dev.marques.domain.dto.ProposalCreditDto;
import system.dev.marques.domain.dto.ProposalDto;
import system.dev.marques.mapper.ProposalMapper;
import system.dev.marques.service.ProducerService;
import system.dev.marques.service.ProposalService;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.*;
import static system.dev.marques.util.QueueDtoCreator.*;

@ExtendWith(MockitoExtension.class)
class ProposalListenerTest {

    @InjectMocks
    private ProposalListener listener;

    @Mock
    private ProposalService serviceMock;

    @Mock
    private ProposalMapper mapperMock;

    @Mock
    private ProducerService producerMock;

    @Test
    void listenProposalQueue_SendProposalToCredit_WhenSuccessful() {
        when(serviceMock.save(any(ProposalDto.class))).thenReturn(createProposal());
        when(mapperMock.toProposalCreditDto(any(Proposal.class))).thenReturn(createProposalCreditDto());
        doNothing().when(producerMock).sendProposalToCredit(any(ProposalCreditDto.class));

        ProposalDto dto = createProposalDto();

        assertThatCode(() -> listener.listenProposalQueue(dto)).doesNotThrowAnyException();

    }

    @Test
    void listenProposalQueue_DoNothing_WhenSavedProposalIsNull() {
        when(serviceMock.save(any(ProposalDto.class))).thenReturn(null);

        ProposalDto dto = createProposalDto();

        assertThatCode(() -> listener.listenProposalQueue(dto)).doesNotThrowAnyException();

    }

    @Test
    void listenProposalQueue_updateProposalStatus_WhenSuccessful() {
        doNothing().when(serviceMock).updateProposalStatus(any(AnalyzedDto.class));

        AnalyzedDto dto = createAnalyzedDto();

        assertThatCode(() -> listener.listenAnalyzedProposalQueue(dto)).doesNotThrowAnyException();

    }


    @Test
    void listenProposalQueue_deleteUserHistory_WhenSuccessful() {
        doNothing().when(serviceMock).deleteUserHistory(anyLong());

        assertThatCode(() -> listener.listenProposalDeleteQueue(1L)).doesNotThrowAnyException();
    }

}