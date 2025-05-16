package system.dev.marques.config.google;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import system.dev.marques.domain.Roles;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.domain.dto.responses.TokenLoginResponse;
import system.dev.marques.domain.dto.responses.UserResponse;
import system.dev.marques.mapper.UserMapper;
import system.dev.marques.service.TokenService;
import system.dev.marques.service.UserService;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Log4j2
public class SocialLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Value("${google.default.password}")
    private String googlePassword;

    private final UserService userService;

    private final TokenService tokenService;

    private final UserMapper mapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        OAuth2AuthenticationToken auth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = auth2AuthenticationToken.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        User user = userService.findByEmail(email)
                .orElseGet(() -> {
                    try {
                        return saveUser(email);
                    } catch (BadRequestException e) {
                        throw new IllegalArgumentException("Error while saving the user", e);
                    }
                });

        TokenLoginResponse jwt = tokenService.generateToken(user);

        log.info("Login com sucesso para o usuário: {}", email);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter()
                .write("{\"token\":\"" + jwt.getToken() + "\", \"expirationTime\":\"" + jwt.getExpiresIn() + "\"}");
        response.getWriter().flush();
    }

    private User saveUser(String email) throws BadRequestException {
        UserRequest userRequest = UserRequest.builder()
                .email(email)
                .name(getName(email))
                .password(googlePassword)
                .build();
        UserResponse userResponse = userService.saveUser(userRequest, "google");

        User user = mapper.toUser(userResponse);
        user.setRoles(Set.of(new Roles(2L, "USER")));
        return user;
    }

    private String getName(String email) {
        return email.substring(0, email.indexOf("@"));
    }
}
