package system.dev.marques.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import system.dev.marques.strategy.impl.GoodScoreStrategy;
import system.dev.marques.strategy.impl.HighScoreStrategy;
import system.dev.marques.strategy.impl.LowScoreStrategy;
import system.dev.marques.strategy.impl.MediumScoreStrategy;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.anyInt;
import static org.mockito.BDDMockito.when;

@ExtendWith(MockitoExtension.class)
class CreditStrategyFactoryTest {

    @InjectMocks
    private CreditStrategyFactory factory;

    @Mock
    private LowScoreStrategy lowScoreMock;

    @Mock
    private HighScoreStrategy highScoreMock;

    @Mock
    private GoodScoreStrategy goodScoreMock;

    @Mock
    private MediumScoreStrategy mediumScoreMock;

    @BeforeEach
    void setUp() {
        factory = new CreditStrategyFactory(List.of(lowScoreMock, mediumScoreMock, goodScoreMock, highScoreMock));
    }


    @Test
    void getStrategy_ReturnsLowScoreStrategy_WhenIsLowScore() {
        when(lowScoreMock.supports(anyInt())).thenReturn(true);

        CreditAnalysisStrategy strategy = factory.getStrategy(1);

        assertThat(strategy).isInstanceOf(LowScoreStrategy.class);

    }

    @Test
    void getStrategy_ReturnsMediumScoreStrategy_WhenScoreIsMedium() {
        when(mediumScoreMock.supports(anyInt())).thenReturn(true);

        CreditAnalysisStrategy strategy = factory.getStrategy(500);

        assertThat(strategy).isInstanceOf(MediumScoreStrategy.class);

    }

    @Test
    void getStrategy_ReturnsGoodScoreStrategy_WhenIsGoodScore() {
        when(goodScoreMock.supports(anyInt())).thenReturn(true);

        CreditAnalysisStrategy strategy = factory.getStrategy(950);

        assertThat(strategy).isInstanceOf(GoodScoreStrategy.class);

    }

    @Test
    void getStrategy_ReturnsHighScoreStrategy_WhenIsHighScore() {
        when(highScoreMock.supports(anyInt())).thenReturn(true);

        CreditAnalysisStrategy strategy = factory.getStrategy(1000);

        assertThat(strategy).isInstanceOf(HighScoreStrategy.class);

    }

    @Test
    void getStrategy_ReturnsIllegalStateException_WhenNoStrategyIsFound() {
        when(highScoreMock.supports(anyInt())).thenReturn(false);
        when(lowScoreMock.supports(anyInt())).thenReturn(false);
        when(goodScoreMock.supports(anyInt())).thenReturn(false);
        when(mediumScoreMock.supports(anyInt())).thenReturn(false);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> factory.getStrategy(500))
                .withMessageContaining("None strategy found for score: ");

    }

}