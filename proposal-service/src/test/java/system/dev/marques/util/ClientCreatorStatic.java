package system.dev.marques.util;

import system.dev.marques.domain.Client;

public class ClientCreatorStatic {


    public static Client createSavedClient() {
        return Client.builder()
                .id(1L)
                .clientId("test-client")
                .clientSecret("test-secret")
                .scope("ADMIN")
                .build();
    }


    public static Client createClient() {
        return Client.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .scope("ADMIN")
                .build();
    }



}
