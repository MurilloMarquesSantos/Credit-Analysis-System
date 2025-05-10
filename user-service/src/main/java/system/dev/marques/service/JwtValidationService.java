package system.dev.marques.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class JwtValidationService {

    public boolean isJwtValid(Jwt token) {
        Boolean isValid = token.getClaim("valid");
        return Boolean.TRUE.equals(isValid);
    }
}
