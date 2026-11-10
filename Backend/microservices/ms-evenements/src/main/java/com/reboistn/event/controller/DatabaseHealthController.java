package com.reboistn.event.controller;

import com.reboistn.event.dto.DatabaseHealthResponse;
import com.reboistn.event.service.DatabaseHealthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Database Check", description = "Vérification de la connectivité avec la base H2")
public class DatabaseHealthController {

    private final DatabaseHealthService databaseHealthService;

    public DatabaseHealthController(DatabaseHealthService databaseHealthService) {
        this.databaseHealthService = databaseHealthService;
    }

    @GetMapping({"/api/events/db-check", "/api/evenements/db-check"})
    @Operation(
            summary = "Vérification de la base de données H2",
            description = "Exécute une requête SQL SELECT 1 via JdbcTemplate pour valider la connexion effective à H2"
    )
    @ApiResponse(responseCode = "200", description = "Connexion à la base H2 réussie")
    public ResponseEntity<DatabaseHealthResponse> checkDatabase() {
        DatabaseHealthResponse response = databaseHealthService.checkDatabaseConnection();
        return ResponseEntity.ok(response);
    }
}
