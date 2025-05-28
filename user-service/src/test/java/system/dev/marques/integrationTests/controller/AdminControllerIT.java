package system.dev.marques.integrationTests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import system.dev.marques.domain.dto.responses.UserAdminResponse;
import system.dev.marques.integrationTests.AbstractIntegration;
import system.dev.marques.repository.RolesRepository;
import system.dev.marques.repository.UserRepository;
import system.dev.marques.util.UserCreator;
import system.dev.marques.wrapper.PageResponse;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=8883")
class AdminControllerIT extends AbstractIntegration {


    private static RequestSpecification spec;

    private static ObjectMapper mapper;

    @Autowired
    private UserCreator userCreator;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private RolesRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    private static User savedUser;

    private static User savedAdmin;

    @BeforeAll
    static void initAll() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @BeforeEach
    void setUp() {

        if (!roleRepository.existsByName("ADMIN")) {
            roleRepository.save(new Roles(null, "ADMIN"));
        }
        if (!roleRepository.existsByName("USER")) {
            roleRepository.save(new Roles(null, "USER"));
        }

        savedAdmin = userRepository.save(userCreator.createAdmin());

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
    void list_ReturnsPageOfUsers_WhenSuccessful() throws JsonProcessingException {

        String token = generateToken(savedAdmin);

        spec = new RequestSpecBuilder()
                .setBasePath("/home/admin/list")
                .setPort(8883)
                .build();

        String getResponse = RestAssured.given()
                .spec(spec)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().body().asString();

        PageResponse<UserAdminResponse> userPage = mapper.readValue(getResponse, new TypeReference<>() {
        });

        assertThat(userPage).isNotNull();

        assertThat(userPage.getContent()).isNotNull();

        assertThat(userPage.getContent()).hasSize(2);
    }

    @Test
    void list_ReturnsForbidden403_WhenUserIsNotAdmin() {

        String token = generateToken(savedUser);

        spec = new RequestSpecBuilder()
                .setBasePath("/home/admin/list")
                .setPort(8883)
                .build();

        RestAssured.given()
                .spec(spec)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get()
                .then()
                .statusCode(403);
    }

    @Test
    void getUserById_ReturnsUser_WhenSuccessful() throws JsonProcessingException {

        String token = generateToken(savedAdmin);

        spec = new RequestSpecBuilder()
                .setBasePath("/home/admin/list/{id}")
                .setPort(8883)
                .build();

        String getResponse = RestAssured.given()
                .spec(spec)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .pathParam("id", savedUser.getId())
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().body().asString();

        UserAdminResponse response = mapper.readValue(getResponse, UserAdminResponse.class);

        assertThat(response).isNotNull();

        assertThat(response.getId()).isEqualTo(savedUser.getId());

        assertThat(response.getName()).isEqualTo(savedUser.getName());

        assertThat(response.getRoles()).isNotNull().isNotEmpty().hasSize(1);

    }

    @Test
    void getUserById_ReturnsForbidden403_WhenUserIsNotAdmin() {

        String token = generateToken(savedUser);

        spec = new RequestSpecBuilder()
                .setBasePath("/home/admin/list/{id}")
                .setPort(8883)
                .build();

        RestAssured.given()
                .spec(spec)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .pathParam("id", savedUser.getId())
                .when()
                .get()
                .then()
                .statusCode(403);
    }

    @Test
    void getUserById_ReturnsBadRequest400_WhenUserIdDoesNotMatches() {

        String token = generateToken(savedAdmin);

        spec = new RequestSpecBuilder()
                .setBasePath("/home/admin/list/{id}")
                .setPort(8883)
                .build();

        RestAssured.given()
                .spec(spec)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .pathParam("id", 10L)
                .when()
                .get()
                .then()
                .statusCode(400);
    }

    @Test
    void deleteUser_RemovesUser_WhenSuccessful() {

        String token = generateToken(savedAdmin);

        spec = new RequestSpecBuilder()
                .setBasePath("/home/admin/list/user/{id}")
                .setPort(8883)
                .build();

        RestAssured.given()
                .spec(spec)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .pathParam("id", savedUser.getId())
                .when()
                .delete()
                .then()
                .statusCode(204);

        Optional<User> userOpt = userRepository.findById(savedUser.getId());

        assertThat(userOpt).isEmpty();
    }

    @Test
    void deleteUser_ReturnsForbidden403_WhenUserIsNotAdmin() {

        String token = generateToken(savedUser);

        spec = new RequestSpecBuilder()
                .setBasePath("/home/admin/list/user/{id}")
                .setPort(8883)
                .build();

        RestAssured.given()
                .spec(spec)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .pathParam("id", savedUser.getId())
                .when()
                .delete()
                .then()
                .statusCode(403);
    }

    @Test
    void deleteUser_ReturnsBadRequest400_WhenUserIdDoesNotMatches() {

        String token = generateToken(savedAdmin);

        spec = new RequestSpecBuilder()
                .setBasePath("/home/admin/list/user/{id}")
                .setPort(8883)
                .build();

        RestAssured.given()
                .spec(spec)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .pathParam("id", 10L)
                .when()
                .delete()
                .then()
                .statusCode(400);
    }
}
