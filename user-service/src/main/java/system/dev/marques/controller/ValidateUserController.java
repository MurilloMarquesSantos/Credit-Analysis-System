package system.dev.marques.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import system.dev.marques.domain.dto.requests.UserEnableRequest;
import system.dev.marques.domain.dto.requests.UserRequestGoogle;
import system.dev.marques.domain.dto.responses.UserEnabledResponse;
import system.dev.marques.service.UserService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("validate")
public class ValidateUserController {

    private final UserService userService;

    @PostMapping("/google")
    public ResponseEntity<UserEnabledResponse> enableUserGoogle(@RequestParam String token,
                                                                @RequestBody @Valid UserRequestGoogle requestGoogle
            , Principal principal) throws Exception {
        return new ResponseEntity<>(userService.enableUserFromGoogle(token, requestGoogle, principal),
                HttpStatus.CREATED);
    }

    @PostMapping("/form-login")
    public ResponseEntity<UserEnabledResponse> enableUserForm(@RequestParam String token,
                                                              @RequestBody @Valid UserEnableRequest request
            , Principal principal) throws Exception {
        return new ResponseEntity<>(userService.enableUser(token, request, principal), HttpStatus.CREATED);
    }

}
