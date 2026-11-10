package ms_inscriptions.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/** Métadonnées globales de la documentation OpenAPI. */
@Configuration
@OpenAPIDefinition(info = @Info(
		title = "API ms-inscriptions",
		version = "1.0.0",
		description = "API de gestion des inscriptions des bénévoles aux événements ReboisTN."))
public class OpenApiConfiguration {
}
