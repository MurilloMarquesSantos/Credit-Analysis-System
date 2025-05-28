package system.dev.marques.integrationTests.controller;


import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import system.dev.marques.domain.Roles;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.responses.TokenLoginResponse;
import system.dev.marques.integrationTests.AbstractIntegration;
import system.dev.marques.repository.RolesRepository;
import system.dev.marques.repository.UserRepository;
import system.dev.marques.util.UserCreator;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8884"})
class LoginControllerIT extends AbstractIntegration {

    private static User savedUser;

    @Autowired
    private UserCreator userCreator;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private RequestSpecification spec;

    @BeforeEach
    void setUp() {
        if (!rolesRepository.existsByName("USER")) {
            rolesRepository.save(new Roles(null, "USER"));
        }

        User rawUser = userCreator.createUser();
        rawUser.setPassword(passwordEncoder.encode("123456"));
        savedUser = userRepository.save(rawUser);

        spec = new RequestSpecBuilder()
                .setPort(8884)
                .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        rolesRepository.deleteAll();
    }

    @Test
    void loginWithForm_ReturnsToken_WhenSuccessful() {
        Response loginResponse = RestAssured.given()
                .spec(spec)
                .contentType("application/x-www-form-urlencoded")
                .formParam("username", savedUser.getEmail())
                .formParam("password", "123456")
                .post("/login");

        assertThat(loginResponse.getStatusCode()).isEqualTo(302);

        String sessionId = loginResponse.getCookie("JSESSIONID");

        assertThat(sessionId).isNotBlank();

        TokenLoginResponse tokenResponse = RestAssured.given()
                .spec(spec)
                .cookie("JSESSIONID", sessionId)
                .get("/")
                .then()
                .log().all()
                .statusCode(200)
                .log().all()
                .extract()
                .as(TokenLoginResponse.class);

        assertThat(tokenResponse.getToken()).isNotBlank();

        assertThat(tokenResponse.getExpiresIn()).isEqualTo(600L);
    }

    @Test
    void login_RedirectToError_WhenCredentialsNotProvidedCorrectly() {
        Response response = RestAssured.given()
                .spec(spec)
                .contentType("application/x-www-form-urlencoded")
                .formParam("username", "wrong@email.com")
                .formParam("password", "wrongpassword")
                .post("/login");

        assertThat(response.getStatusCode()).isEqualTo(302);

        assertThat(response.getHeader("Location")).contains("/login?error");

    }
}

