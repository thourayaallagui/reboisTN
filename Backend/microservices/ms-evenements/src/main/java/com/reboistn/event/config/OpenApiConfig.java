package com.reboistn.event.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI eventServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ReboisTN - MS1 Event Service API")
                        .description("Documentation OpenAPI / Swagger du microservice MS1 – Événements de la plateforme ReboisTN.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Équipe ReboisTN - MT-41 AWD")
                                .email("contact@reboistn.tn"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")));
    }
}
