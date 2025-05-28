package system.dev.marques.util;


import system.dev.marques.domain.EnableUserToken;

import java.time.LocalDateTime;
import java.util.UUID;

public class EnableTokenCreator {

    public static EnableUserToken createUserToken() {
        return EnableUserToken.builder()
                .userId(1L)
                .token(UUID.randomUUID().toString())
                .expirationDate(LocalDateTime.now())
                .build();
    }
}
