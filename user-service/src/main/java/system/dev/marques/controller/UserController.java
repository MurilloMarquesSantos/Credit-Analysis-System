package system.dev.marques.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;
import system.dev.marques.domain.dto.UserRequest;
import system.dev.marques.domain.dto.UserResponse;
import system.dev.marques.service.UserService;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class UserController {

    private final JwtEncoder jwtEncoder;

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(userService.save(userRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public String teste() {

        JwtClaimsSet claimSet = JwtClaimsSet.builder()
                .issuer("Backend")
                .subject("1")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(600))
                .claim("scope", "teste")
                .build();

        String tokenValue = jwtEncoder.encode(JwtEncoderParameters.from(claimSet)).getTokenValue();

        return tokenValue;
    }

}
