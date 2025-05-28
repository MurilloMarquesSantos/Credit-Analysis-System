package system.dev.marques.integrationTests.controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import system.dev.marques.domain.Roles;
import system.dev.marques.domain.User;
import system.dev.marques.integrationTests.AbstractIntegration;
import system.dev.marques.repository.RolesRepository;
import system.dev.marques.repository.UserRepository;
import system.dev.marques.util.UserCreator;

import java.time.Instant;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static system.dev.marques.util.RequestCreator.createInvalidProposalRequest;
import static system.dev.marques.util.RequestCreator.createProposalRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8885"})
class ProposalControllerIT extends AbstractIntegration {


    private static User savedUser;

    @Autowired
    private UserCreator userCreator;

    @Autowired
    private RolesRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtEncoder jwtEncoder;


    @BeforeEach
    void setUp() {
        if (!roleRepository.existsByName("ADMIN")) {
            roleRepository.save(new Roles(null, "ADMIN"));
        }
        if (!roleRepository.existsByName("USER")) {
            roleRepository.save(new Roles(null, "USER"));
        }

        savedUser = userRepository.save(userCreator.createUser());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
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
    void proposal_SendProposalWhen_Successful() {
        String token = generateToken(savedUser);

        RequestSpecification spec = new RequestSpecBuilder()
                .setBasePath("/home/send-proposal")
                .setPort(8885)
                .build();

        String response = RestAssured.given()
                .spec(spec)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createProposalRequest())
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        assertThat(response).isNotBlank().isEqualTo(
                "Proposal sent for review. Please monitor your email inbox for further updates.");

    }

    @Test
    void proposal_ReturnsBadRequest400When_RequestHasNullFields() {
        String token = generateToken(savedUser);

        RequestSpecification spec = new RequestSpecBuilder()
                .setBasePath("/home/send-proposal")
                .setPort(8885)
                .build();

        RestAssured.given()
                .spec(spec)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createInvalidProposalRequest())
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    void proposal_ReturnsUnauthorized401_WhenTokenIsInvalid() {

        RequestSpecification spec = new RequestSpecBuilder()
                .setBasePath("/home/send-proposal")
                .setPort(8885)
                .build();

        RestAssured.given()
                .spec(spec)
                .contentType("application/json")
                .body(createProposalRequest())
                .when()
                .post()
                .then()
                .statusCode(401);
    }


}
