package com.reboistn.event.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Réponse de vérification de connectivité avec la base de données")
public class DatabaseHealthResponse {

    @Schema(description = "Type de base de données", example = "H2")
    private String database;

    @Schema(description = "Statut de la connexion", example = "CONNECTED")
    private String status;

    @Schema(description = "Résultat de la requête de test (SELECT 1)", example = "1")
    private Integer result;

    public DatabaseHealthResponse() {
    }

    public DatabaseHealthResponse(String database, String status, Integer result) {
        this.database = database;
        this.status = status;
        this.result = result;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
