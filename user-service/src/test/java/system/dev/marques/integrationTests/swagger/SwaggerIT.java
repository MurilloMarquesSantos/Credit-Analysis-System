package system.dev.marques.integrationTests.swagger;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import system.dev.marques.integrationTests.AbstractIntegration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=1010")
class SwaggerIT extends AbstractIntegration {

    @Test
    void shouldDisplaySwaggerUIPage() {
        String returnedContent = RestAssured.given()
                .basePath("/swagger-ui/index.html")
                .port(1010)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().body().asString();

        assertTrue(returnedContent.contains("Swagger UI"));
    }
}
