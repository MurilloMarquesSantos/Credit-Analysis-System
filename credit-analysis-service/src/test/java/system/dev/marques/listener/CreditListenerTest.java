package system.dev.marques.listener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import system.dev.marques.dto.AnalyzedDto;
import system.dev.marques.dto.ProposalCreditDto;
import system.dev.marques.service.CreditAnalysisService;
import system.dev.marques.service.ProducerService;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.*;
import static system.dev.marques.util.QueueDtoCreator.createAnalyzedDto;
import static system.dev.marques.util.QueueDtoCreator.createProposalCreditDto;

@ExtendWith(MockitoExtension.class)
class CreditListenerTest {

    @InjectMocks
    private CreditListener listener;

    @Mock
    private CreditAnalysisService serviceMock;

    @Mock
    private ProducerService producerMock;


    @Test
    void listenCreditProposal_SendsAnalyzedProposal_WhenSuccessful() {

        when(serviceMock.analyzeCredit(any(ProposalCreditDto.class))).thenReturn(createAnalyzedDto());
        doNothing().when(producerMock).sendAnalyzedProposal(any(AnalyzedDto.class));

        ProposalCreditDto dto = createProposalCreditDto();

        assertThatCode(() -> listener.listenCreditProposal(dto)).doesNotThrowAnyException();

        verify(producerMock, times(1)).sendAnalyzedProposal(any(AnalyzedDto.class));

    }

}