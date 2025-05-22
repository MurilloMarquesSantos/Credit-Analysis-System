package system.dev.marques.integrationTests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import system.dev.marques.domain.Roles;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.requests.UserEnableRequest;
import system.dev.marques.domain.dto.requests.UserRequestGoogle;
import system.dev.marques.domain.dto.responses.UserEnabledResponse;
import system.dev.marques.integrationTests.AbstractIntegration;
import system.dev.marques.repository.RolesRepository;
import system.dev.marques.repository.UserRepository;
import system.dev.marques.service.TokenService;
import system.dev.marques.util.UserCreator;

import java.time.Instant;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8886"})
class ValidateUserControllerIT extends AbstractIntegration {

    private static RequestSpecification spec;

    private static ObjectMapper mapper;

    private static String enableToken;

    private static User savedUser;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserCreator userCreator;

    @Autowired
    private RolesRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    static void setUpBeforeAll() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @BeforeEach
    void setUp() {

        if (!roleRepository.existsByName("ADMIN")) {
            roleRepository.save(new Roles(null, "ADMIN"));
        }
        if (!roleRepository.existsByName("USER")) {
            roleRepository.save(new Roles(null, "USER"));
        }

        savedUser = userRepository.save(userCreator.createUser());

        enableToken = tokenService.generateEnableUserToken(savedUser);

    }

    @AfterEach
    void setUpAfterEach() {
        userRepository.deleteAll();
    }

    private String generateToken(User user) {
        String scope = user.getRoles()
                .stream()
                .map(Roles::getName)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("test-containers")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .subject(user.getId().toString())
                .claim("scope", scope)
                .claim("valid", user.isValid())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Test
    void enableUserForm_ReturnsEnabledResponse_WhenUserIsEnabled() throws JsonProcessingException {

        UserEnableRequest userEnableRequest = userCreator.createUserEnableRequest();

        String token = generateToken(savedUser);

        spec = new RequestSpecBuilder()
                .setBasePath("/validate/form-login")
                .setPort(8886)
                .build();

        String returnedContent = RestAssured.given()
                .spec(spec)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .queryParam("token", enableToken)
                .body(userEnableRequest)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body().asString();

        UserEnabledResponse response = mapper.readValue(returnedContent, UserEnabledResponse.class);

        assertThat(response).isNotNull();

        assertThat(response.getName()).isEqualTo(savedUser.getName());

        assertThat(response.getEmail()).isEqualTo(savedUser.getEmail());
    }

    @Test
    void enableUserGoogle_ReturnsEnabledResponse_WhenUserIsEnabled() throws JsonProcessingException {

        UserRequestGoogle userEnableRequest = userCreator.createUserRequestGoogle();

        String token = generateToken(savedUser);

        spec = new RequestSpecBuilder()
                .setBasePath("/validate/google")
                .setPort(8886)
                .build();

        String returnedContent = RestAssured.given()
                .spec(spec)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .queryParam("token", enableToken)
                .body(userEnableRequest)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body().asString();

        UserEnabledResponse response = mapper.readValue(returnedContent, UserEnabledResponse.class);

        assertThat(response).isNotNull();

        assertThat(response.getName()).isEqualTo(savedUser.getName());

        assertThat(response.getEmail()).isEqualTo(savedUser.getEmail());
    }


}
