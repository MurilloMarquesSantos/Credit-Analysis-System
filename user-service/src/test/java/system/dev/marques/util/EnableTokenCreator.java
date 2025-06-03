package system.dev.marques.util;


import system.dev.marques.domain.EnableUserToken;

import java.time.LocalDateTime;
import java.util.UUID;

public class EnableTokenCreator {

    public static EnableUserToken createUserToken() {
        return EnableUserToken.builder()
                .id(1L)
                .userId(1L)
                .token(UUID.randomUUID().toString())
                .expirationDate(LocalDateTime.now().plusMinutes(10))
                .build();
    }
}
