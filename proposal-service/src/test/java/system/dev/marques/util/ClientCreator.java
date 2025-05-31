package system.dev.marques.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import system.dev.marques.domain.Client;

@Component
public class ClientCreator {

    @Autowired
    private BCryptPasswordEncoder encoder;

    public Client createClient() {
        return Client.builder()
                .clientId("test-client")
                .clientSecret(encoder.encode("test-secret"))
                .scope("ADMIN")
                .build();
    }

    public Client createNotAdminClient() {
        return Client.builder()
                .clientId("test-client-not-admin")
                .clientSecret(encoder.encode("test-secret-not-admin"))
                .build();
    }
}
