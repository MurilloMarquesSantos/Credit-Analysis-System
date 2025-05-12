package system.dev.marques.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.EnableUserToken;
import system.dev.marques.domain.Roles;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.responses.TokenLoginResponse;
import system.dev.marques.repository.TokenRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtEncoder jwtEncoder;

    private final TokenRepository tokenRepository;


    public TokenLoginResponse generateToken(User user) {

        Instant now = Instant.now();
        long expiresIn = 300L;


        String scope = user.getRoles()
                .stream()
                .map(Roles::getName)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("user-service-backend")
                .subject(String.valueOf(user.getId()))
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scope)
                .claim("valid", user.isValid())
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();

        return TokenLoginResponse.builder().token(token).expiresIn(expiresIn).build();
    }

    public String generateEnableUserToken(User user) {
        String token = UUID.randomUUID().toString();

        EnableUserToken tokenToSave = EnableUserToken.builder()
                .userId(user.getId()).
                token(token)
                .expirationDate(LocalDateTime.now().plusMinutes(10)).build();

        tokenRepository.save(tokenToSave);

        return token;
    }

    public boolean validateToken(String token, Long userId) {
        Optional<EnableUserToken> tokenOpt = tokenRepository.findByToken(token);
        return tokenOpt.isPresent() && tokenOpt.get().getUserId().equals(userId)
                && tokenOpt.get().getExpirationDate().isAfter(LocalDateTime.now());
    }
}

