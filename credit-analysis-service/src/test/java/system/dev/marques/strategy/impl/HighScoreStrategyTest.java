package system.dev.marques.strategy.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import system.dev.marques.dto.AnalyzedDto;
import system.dev.marques.enums.ProposalStatus;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class HighScoreStrategyTest {

    @InjectMocks
    private HighScoreStrategy strategy;

    @Test
    void supports_ReturnsTrue_WhenScoreIsHigherThan950() {

        assertThat(strategy.supports(1000)).isTrue();

    }

    @Test
    void supports_ReturnsFalse_WhenScoreIsLowerThan950() {

        assertThat(strategy.supports(949)).isFalse();

    }

    @Test
    void analyze_ReturnsAnalyzedProposalApproved_WhenSuccessful(){

        AnalyzedDto analyzedDto
                = strategy.analyse(1L, 50.0, 5.0, 300, "", 7);

        assertThat(analyzedDto).isNotNull();

        assertThat(analyzedDto.getStatus()).isEqualTo(ProposalStatus.APPROVED);
    }

    @Test
    void analyze_ReturnsAnalyzedProposalApproved_WithHigherMultiplier_WhenSuccessful(){

        AnalyzedDto analyzedDto
                = strategy.analyse(1L, 50.0, 50.0, 300, "", 80);

        assertThat(analyzedDto).isNotNull();

        assertThat(analyzedDto.getStatus()).isEqualTo(ProposalStatus.APPROVED);
    }

    @Test
    void analyze_ReturnsAnalyzedProposalRejected_WhenRequestedAmountIsGreaterThanMaxValue(){

        AnalyzedDto analyzedDto
                = strategy.analyse(1L, 50.0, 5000.0, 300, "", 2);

        assertThat(analyzedDto).isNotNull();

        assertThat(analyzedDto.getStatus()).isEqualTo(ProposalStatus.REJECTED);

    }

}