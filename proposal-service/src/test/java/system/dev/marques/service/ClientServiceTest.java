package system.dev.marques.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import system.dev.marques.domain.Client;
import system.dev.marques.repository.ClientRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.when;
import static system.dev.marques.util.ClientCreatorStatic.createClient;
import static system.dev.marques.util.ClientCreatorStatic.createSavedClient;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepositoryMock;

    @Mock
    private BCryptPasswordEncoder encoderMock;

    @Test
    void findByClientId_ReturnsClient_WhenSuccessful() {

        when(clientRepositoryMock.findByClientId(anyString())).thenReturn(Optional.of(createSavedClient()));

        Client client = clientService.findByClientId("");

        Client savedClient = createSavedClient();

        assertThat(client).isNotNull().isEqualTo(savedClient);
    }

    @Test
    void findByClientId_ThrowsRuntimeException_WhenClientNotFound() {

        when(clientRepositoryMock.findByClientId(anyString())).thenReturn(Optional.empty());

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> clientService.findByClientId(""))
                .withMessageContaining("Client not found");

    }

    @Test
    void save_ReturnsSavedClient_WhenSuccessful() {
        when(encoderMock.encode(anyString())).thenReturn("encoded");
        when(clientRepositoryMock.save(any(Client.class))).thenReturn(createSavedClient());

        Client client = clientService.save(createClient());

        Client savedClient = createSavedClient();

        assertThat(client).isNotNull().isEqualTo(savedClient);
    }


}