package system.dev.marques.integration.controller;

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
import system.dev.marques.domain.Client;
import system.dev.marques.integration.AbstractIntegration;
import system.dev.marques.repository.ClientRepository;
import system.dev.marques.util.ClientCreator;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=9992")
class ClientControllerIT extends AbstractIntegration {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientCreator clientCreator;

    private static ObjectMapper mapper;

    private static RequestSpecification spec;

    private static String token;

    @BeforeAll
    static void initAll() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @BeforeEach
    void setUp() {

        Client client = clientCreator.createClient();
        clientRepository.save(client);


        token = RestAssured.given()
                .port(9992)
                .auth().preemptive().basic("test-client", "test-secret")
                .contentType("application/x-www-form-urlencoded")
                .formParam("grant_type", "client_credentials")
                .when()
                .post("/oauth2/token")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("access_token");
    }

    @AfterEach
    void tearDown() {
        clientRepository.deleteAll();
    }

    @Test
    void addClient_ReturnsClient_WhenSuccessful() throws JsonProcessingException {

        Client clientToBeSaved = clientCreator.createClientToBeSaved();
        spec = new RequestSpecBuilder()
                .setBasePath("/clients")
                .setPort(9992)
                .build();

        String postResponse = RestAssured.given()
                .spec(spec)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(clientToBeSaved)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        Client response = mapper.readValue(postResponse, Client.class);

        assertThat(response).isNotNull();

        assertThat(response.getId()).isNotNull();

        assertThat(response.getClientId()).isEqualTo(clientToBeSaved.getClientId());

    }

    @Test
    void addClient_ReturnsUnauthorized401_WhenNoTokenIsGiven() {

        Client clientToBeSaved = clientCreator.createClientToBeSaved();

        spec = new RequestSpecBuilder()
                .setBasePath("/clients")
                .setPort(9992)
                .build();

        RestAssured.given()
                .spec(spec)
                .contentType("application/json")
                .body(clientToBeSaved)
                .when()
                .post()
                .then()
                .statusCode(401);

    }

}
