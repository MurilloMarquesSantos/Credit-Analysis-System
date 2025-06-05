package system.dev.marques.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import system.dev.marques.dto.AnalyzedDto;
import system.dev.marques.dto.ProposalCreditDto;
import system.dev.marques.enums.ProposalStatus;
import system.dev.marques.strategy.CreditAnalysisStrategy;
import system.dev.marques.strategy.CreditStrategyFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.when;
import static system.dev.marques.util.QueueDtoCreator.createAnalyzedDto;
import static system.dev.marques.util.QueueDtoCreator.createProposalCreditDto;

@ExtendWith(MockitoExtension.class)
class CreditAnalysisServiceTest {

    @InjectMocks
    private CreditAnalysisService creditAnalysisService;

    @Mock
    private CreditStrategyFactory factoryMock;

    @Mock
    private CreditAnalysisStrategy strategyMock;

    @Test
    void analyzeCredit_ReturnsAnalyzedProposal_WhenSuccessful() {
        when(factoryMock.getStrategy(anyInt())).thenReturn(strategyMock);
        when(strategyMock.analyse(anyLong(), anyDouble(), anyDouble(), anyInt(), anyString(), anyInt()))
                .thenReturn(createAnalyzedDto());

        ProposalCreditDto dto = createProposalCreditDto();

        AnalyzedDto analyzedProposal = creditAnalysisService.analyzeCredit(dto);

        assertThat(analyzedProposal).isNotNull();

        assertThat(analyzedProposal.getStatus()).isEqualTo(ProposalStatus.REJECTED);
    }

}