package system.dev.marques.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Service API")
                        .version("v1")
                        .description("This service allows users to log in via social login or form login. "
                                + "It provides REST endpoints for user registration and management, "
                                + "and communicates with other services using both REST and RabbitMQ.")
                        .contact(new Contact()
                                .name("Murillo Marques")
                                .email("murillomarques2001@gmail.com")
                                .url("https://github.com/MurilloMarquesSantos/Credit-Analysis-System"))
                        .license(new License().name("MIT License").url("https://opensource.org/licenses/MIT"))
                        .termsOfService("https://github.com/MurilloMarquesSantos/Credit-Analysis-System#readme"));
    }
}
