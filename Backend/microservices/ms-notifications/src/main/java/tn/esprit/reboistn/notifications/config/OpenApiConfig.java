package tn.esprit.reboistn.notifications.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration OpenAPI / Swagger pour ms-notifications.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI notificationsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Notifications - ReboisTN")
                        .description("API de gestion des notifications (EMAIL et IN_APP) pour la plateforme ReboisTN.")
                        .version("1.0.0"));
    }
}
