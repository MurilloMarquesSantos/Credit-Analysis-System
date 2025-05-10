package system.dev.marques.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import system.dev.marques.domain.dto.responses.TokenLoginResponse;
import system.dev.marques.service.UserService;

import java.security.Principal;

@Controller
@Log4j2
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<TokenLoginResponse> homePage(Principal principal) {
        return ResponseEntity.ok(userService.createToken(principal));
    }

}
