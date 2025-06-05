package system.dev.marques.strategy.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import system.dev.marques.dto.AnalyzedDto;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class LowScoreStrategyTest {

    @InjectMocks
    private LowScoreStrategy strategy;

    @Test
    void supports_ReturnsTrue_WhenScoreIsLowerThan400() {

        assertThat(strategy.supports(399)).isTrue();

    }

    @Test
    void supports_ReturnsFalse_WhenScoreIsGreaterThan399() {

        assertThat(strategy.supports(400)).isFalse();

    }

    @Test
    void analyze_ReturnsAnalyzedProposal_WhenSuccessful() {

        AnalyzedDto analyzedDto =
                strategy.analyse(1L, 5.0, 5.0, 300, "", 7);

        assertThat(analyzedDto).isNotNull();

        assertThat(analyzedDto.getProposalId()).isEqualTo(1L);
    }

}