package system.dev.marques.integration.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import system.dev.marques.service.CreditAnalysisService;
import system.dev.marques.service.ProducerService;

@TestConfiguration
public class MockConfig {

    @Bean
    public ProducerService producerService() {
        return Mockito.mock(ProducerService.class);
    }

    @Bean
    public CreditAnalysisService creditAnalysisService() {
        return Mockito.mock(CreditAnalysisService.class);
    }
}
