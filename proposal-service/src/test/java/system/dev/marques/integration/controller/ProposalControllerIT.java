package system.dev.marques.integration.controller;

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
import system.dev.marques.domain.Client;
import system.dev.marques.domain.Proposal;
import system.dev.marques.domain.dto.reponse.ProposalHistoryResponse;
import system.dev.marques.integration.AbstractIntegration;
import system.dev.marques.repository.ClientRepository;
import system.dev.marques.repository.ProposalRepository;
import system.dev.marques.util.ClientCreator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static system.dev.marques.util.ProposalCreator.createProposal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=9991")
class ProposalControllerIT extends AbstractIntegration {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private ClientCreator clientCreator;

    private static ObjectMapper mapper;

    private static String token;

    private static RequestSpecification spec;

    private static Proposal savedProposal;

    @BeforeAll
    static void initAll() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @BeforeEach
    void setUp() {
        Proposal proposal = createProposal();
        savedProposal = proposalRepository.save(proposal);

        Client client = clientCreator.createClient();
        clientRepository.save(client);

        Client notAdminClient = clientCreator.createNotAdminClient();
        clientRepository.save(notAdminClient);

        token = RestAssured.given()
                .port(9991)
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
        proposalRepository.deleteAll();
    }

    @Test
    void getProposalHistory_ReturnsListOfProposalHistoryResponse_WhenSuccessful() throws JsonProcessingException {

        spec = new RequestSpecBuilder()
                .setBasePath("/service/history/{id}")
                .setPort(9991)
                .build();

        String getResponse = RestAssured.given()
                .spec(spec)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .pathParam("id", savedProposal.getUserId())
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        List<ProposalHistoryResponse> responseList = mapper.readValue(getResponse, new TypeReference<>() {
        });

        assertThat(responseList).isNotNull().isNotEmpty().hasSize(1);

        assertThat(responseList.getFirst().getProposalId()).isEqualTo(savedProposal.getId());

        assertThat(responseList.getFirst().getCpf()).isEqualTo(savedProposal.getCpf());

    }

    @Test
    void getProposalHistory_ReturnsUnauthorized401_WhenNoTokenIsGiven() {

        spec = new RequestSpecBuilder()
                .setBasePath("/service/history/{id}")
                .setPort(9991)
                .build();

        RestAssured.given()
                .spec(spec)
                .contentType("application/json")
                .when()
                .pathParam("id", savedProposal.getUserId())
                .get()
                .then()
                .statusCode(401);
    }

}
