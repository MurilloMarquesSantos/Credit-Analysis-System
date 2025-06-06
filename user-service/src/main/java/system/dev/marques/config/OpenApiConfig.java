package system.dev.marques.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)))
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
