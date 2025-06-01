package system.dev.marques.integration.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import system.dev.marques.service.S3Service;

@TestConfiguration
public class MockConfig {

    @Bean
    public S3Service s3Service() {
        return Mockito.mock(S3Service.class);
    }
}
