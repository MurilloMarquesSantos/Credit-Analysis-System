package system.dev.marques.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.domain.dto.responses.UserResponse;
import system.dev.marques.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class UserController {

    private final JwtEncoder jwtEncoder;

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest userRequest) throws BadRequestException {
        return new ResponseEntity<>(userService.saveUser(userRequest), HttpStatus.CREATED);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<UserResponse> createAdmin(@RequestBody UserRequest userRequest) throws BadRequestException {
        return new ResponseEntity<>(userService.saveAdmin(userRequest), HttpStatus.CREATED);
    }

}
