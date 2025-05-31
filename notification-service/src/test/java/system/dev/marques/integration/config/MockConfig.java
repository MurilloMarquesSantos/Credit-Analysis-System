package system.dev.marques.integration.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import system.dev.marques.service.EmailService;

@TestConfiguration
public class MockConfig {

    @Bean
    public EmailService emailService() {
        return Mockito.mock(EmailService.class);
    }
}
