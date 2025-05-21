package system.dev.marques.integrationTests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import system.dev.marques.domain.Roles;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.domain.dto.responses.UserResponse;
import system.dev.marques.integrationTests.AbstractIntegration;
import system.dev.marques.repository.RolesRepository;
import system.dev.marques.repository.UserRepository;

import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=8888")
class UserControllerIT extends AbstractIntegration {

    private static RequestSpecification spec;
    private static ObjectMapper mapper;
    private static UserRequest userRequest;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private RolesRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    private static User savedUser;

    @BeforeEach
    void setUp() {

        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        spec = new RequestSpecBuilder()
                .setBasePath("/home/create")
                .setPort(8888)
                .build();

        userRequest = UserRequest.builder().email("murillomarques@gmail.com")
                .name("Murillo Marques")
                .cpf("13912947856")
                .password("Murillo@1212")
                .phoneNumber("11995147830")
                .build();

        if (!roleRepository.existsByName("ADMIN")) {
            roleRepository.save(new Roles(null, "ADMIN"));
        }
        if (!roleRepository.existsByName("USER")) {
            roleRepository.save(new Roles(null, "USER"));
        }
        User user = User.builder()
                .cpf("54529459896")
                .email("murillo@gmail.com")
                .name("Murillo Marques")
                .phoneNumber("11995147833")
                .password(passwordEncoder.encode("Murillo@12345"))
                .roles(Set.of(roleRepository.findByName("ADMIN").orElseThrow()))
                .valid(true)
                .build();

        savedUser = userRepository.save(user);
    }

    private String generateAdminToken(User user) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("test-containers")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .subject(user.getId().toString())
                .claim("scope", "ADMIN")
                .claim("valid", user.isValid())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Test
    void create_ReturnsUser_WhenSuccessful() throws JsonProcessingException {
        String token = generateAdminToken(savedUser);

        String returnedContent = RestAssured.given()
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


        UserResponse userResponse = mapper.readValue(returnedContent, UserResponse.class);

        assertThat(userResponse.getEmail()).isEqualTo(userRequest.getEmail());

    }
}
