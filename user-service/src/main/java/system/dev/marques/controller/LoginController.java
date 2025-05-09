package system.dev.marques.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@Log4j2
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping
    @ResponseBody
    public String homePage(Authentication auth, Principal principal) {
        log.info(principal.getName());
        log.info(principal);
        return "Olá " + auth.getName();
    }

}
