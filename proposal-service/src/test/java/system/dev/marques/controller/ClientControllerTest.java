package system.dev.marques.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import system.dev.marques.domain.Client;
import system.dev.marques.service.ClientService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.when;
import static system.dev.marques.util.ClientCreatorStatic.createClient;
import static system.dev.marques.util.ClientCreatorStatic.createSavedClient;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @InjectMocks
    private ClientController controller;

    @Mock
    private ClientService clientServiceMock;

    @Test
    void addClient_ReturnsClient_WhenSuccessful() {
        Client savedClient = createSavedClient();
        when(clientServiceMock.save(any())).thenReturn(savedClient);

        Client client = createClient();
        ResponseEntity<Client> responseEntity = controller.addClient(client);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getId()).isNotNull();
    }

}