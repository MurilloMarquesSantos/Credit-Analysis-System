package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.Client;
import system.dev.marques.repository.ClientRepository;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    private final BCryptPasswordEncoder encoder;

    public Client findByClientId(String clientId) {
        return clientRepository.findByClientId(clientId).orElseThrow(() -> new RuntimeException("Client not found"));
    }

    public Client save(Client client) {
        client.setClientSecret(encoder.encode(client.getClientSecret()));
        return clientRepository.save(client);
    }
}
