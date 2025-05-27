package system.dev.marques.integrationTests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import system.dev.marques.domain.Roles;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.domain.dto.responses.ProposalHistoryResponse;
import system.dev.marques.domain.dto.responses.UserResponse;
import system.dev.marques.integrationTests.AbstractIntegration;
import system.dev.marques.integrationTests.config.WebClientTestConfig;
import system.dev.marques.repository.RolesRepository;
import system.dev.marques.repository.UserRepository;
import system.dev.marques.util.UserCreator;
import system.dev.marques.wrapper.PageResponse;

import java.time.Instant;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Import(WebClientTestConfig.class)
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=8888")
class UserControllerIT extends AbstractIntegration {

    private static RequestSpecification spec;

    private static ObjectMapper mapper;

    private static UserRequest userRequest;

    private static User savedUser;

    private static User savedAdmin;

    private static User savedInvalidUser;

    @Autowired
    private UserCreator userCreator;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private RolesRepository roleRepository;

    @Autowired
    private UserRepository userRepository;


    @BeforeAll
    static void initAll() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @BeforeEach
    void setUpBeforeEach() {
        userRequest = userCreator.createUserRequest();

        if (!roleRepository.existsByName("ADMIN")) {
            roleRepository.save(new Roles(null, "ADMIN"));
        }
        if (!roleRepository.existsByName("USER")) {
            roleRepository.save(new Roles(null, "USER"));
        }

        savedAdmin = userRepository.save(userCreator.createAdmin());
        savedUser = userRepository.save(userCreator.createUser());
        savedInvalidUser = userRepository.save(userCreator.createInvalidUser());

        WireMock wireMockClient = new WireMock(wiremock.getHost(), wiremock.getFirstMappedPort());

        wireMockClient.register(
                get(urlPathMatching("/service/history/\\d+"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                                            {
                                              "content": [
                                                {
                                                  "proposalId": 1,
                                                  "cpf": "123456789",
                                                  "requestedAmount": 5000.0,
                                                  "status": "APPROVED",
                                                  "createdAt": "2024-05-01T12:00:00"
                                                }
                                              ],
                                              "number": 0,
                                              "size": 5,
                                              "totalElements": 1,
                                              "totalPages": 1,
                                              "last": false,
                                              "first": true,
                                              "empty": false
                                            }
                                        """)
                                .withStatus(200))
        );
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
    void create_ReturnsUser_WhenSuccessful() throws JsonProcessingException {
        spec = new RequestSpecBuilder()
                .setBasePath("/home/create")
                .setPort(8888)
                .build();

        String postResponse = RestAssured.given()
                .spec(spec)
                .contentType("application/json")
                .body(userRequest)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body().asString();

        UserResponse userResponse = mapper.readValue(postResponse, UserResponse.class);

        assertThat(userResponse.getEmail()).isEqualTo(userRequest.getEmail());
    }

    @Test
    void createAdmin_ReturnsUserAdmin_WhenSuccessful() throws JsonProcessingException {
        String token = generateToken(savedAdmin);

        spec = new RequestSpecBuilder()
                .setBasePath("/home/admin/create-admin")
                .setPort(8888)
                .build();

        String postResponse = RestAssured.given()
                .spec(spec)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(userRequest)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body().asString();


        UserResponse userResponse = mapper.readValue(postResponse, UserResponse.class);

        assertThat(userResponse.getEmail()).isEqualTo(userRequest.getEmail());
    }

    @Test
    void createAdmin_ReturnsUnauthorized401_WhenUserIsNotAdmin() {
        String token = generateToken(savedUser);

        spec = new RequestSpecBuilder()
                .setBasePath("/home/admin/create-admin")
                .setPort(8888)
                .build();

        RestAssured.given()
                .spec(spec)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(userRequest)
                .when()
                .post()
                .then()
                .statusCode(403);
    }

    @Test
    void getUserHistory_ReturnsHistory_WhenSuccessful() throws JsonProcessingException {
        String token = generateToken(savedAdmin);

        spec = new RequestSpecBuilder()
                .setBasePath("/home/user/history")
                .setPort(8888)
                .build();

        String getResponse = RestAssured.given()
                .spec(spec)
                .queryParam("page", 0)
                .queryParam("size", 5)
                .header("Authorization", "Bearer " + token)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        PageResponse<ProposalHistoryResponse> page = mapper.readValue(
                getResponse, new TypeReference<>() {
                });

        assertThat(page.getContent()).hasSize(1);
    }

    @Test
    void getUserHistory_ReturnForbidden403_WhenUserIsNotValid() {
        String token = generateToken(savedInvalidUser);

        spec = new RequestSpecBuilder()
                .setBasePath("/home/user/history")
                .setPort(8888)
                .build();

        RestAssured.given()
                .spec(spec)
                .queryParam("page", 0)
                .queryParam("size", 5)
                .header("Authorization", "Bearer " + token)
                .get()
                .then()
                .statusCode(403);
    }

    @Test
    void getUserProposalReceipt_SendReceipt_WhenSuccessful(){
        String token = generateToken(savedAdmin);

        spec = new RequestSpecBuilder()
                .setBasePath("/home/user/history/{id}")
                .setPort(8888)
                .build();

        String getResponse = RestAssured.given()
                .spec(spec)
                .header("Authorization", "Bearer " + token)
                .pathParam("id", 1L)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        assertThat(getResponse).isEqualTo("Request processed successfully, stay alert on your email box.");
    }

}
