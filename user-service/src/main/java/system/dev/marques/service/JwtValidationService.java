package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtValidationService {

    private final UserService userService;

    public boolean isJwtValid(Jwt token) {
        boolean isValid = Boolean.TRUE.equals(token.getClaim("valid"));
        if (!isValid) {
            userService.notifyUser(token);
        }
        return isValid;
    }
}
