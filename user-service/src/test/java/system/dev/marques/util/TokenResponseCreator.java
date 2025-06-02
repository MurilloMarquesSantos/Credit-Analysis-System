package system.dev.marques.util;

import system.dev.marques.domain.dto.responses.TokenLoginResponse;

public class TokenResponseCreator {

    public static TokenLoginResponse createTokenLoginResponse() {
        return TokenLoginResponse.builder()
                .token("")
                .expiresIn(300L)
                .build();

    }
}
