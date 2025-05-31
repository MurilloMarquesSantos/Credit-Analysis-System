package system.dev.marques.integration.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import system.dev.marques.domain.Client;
import system.dev.marques.integration.AbstractIntegration;
import system.dev.marques.repository.ClientRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClientRepositoryIT extends AbstractIntegration {

    @Autowired
    private ClientRepository clientRepository;

    private static Client client;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .clientId("test-client")
                .clientSecret(("test-secret"))
                .scope("ADMIN")
                .build();

    }

    @AfterEach
    void tearDown() {
        clientRepository.deleteAll();
    }

    @Test
    void save_PersistsClient_WhenSuccessful() {

        Client savedClient = clientRepository.save(client);

        assertThat(savedClient.getId()).isNotNull();

        assertThat(savedClient.getClientId()).isEqualTo(client.getClientId());

    }

    @Test
    void findByClientId_ReturnsClientOptional_WhenSuccessful() {

        clientRepository.save(client);

        Optional<Client> clientOpt = clientRepository.findByClientId(client.getClientId());

        assertThat(clientOpt).isPresent();

        assertThat(clientOpt.get().getClientId()).isEqualTo(client.getClientId());
    }

    @Test
    void findByClientId_ReturnsEmptyClientOptional_WhenClientIsNotFound() {

        Optional<Client> clientOpt = clientRepository.findByClientId("");

        assertThat(clientOpt).isNotPresent();

    }
}
