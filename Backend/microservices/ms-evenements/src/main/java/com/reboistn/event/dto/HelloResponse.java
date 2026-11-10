package com.reboistn.event.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Réponse de test du service")
public class HelloResponse {

    @Schema(description = "Identifiant du service", example = "event-service")
    private String service;

    @Schema(description = "Statut du service", example = "UP")
    private String status;

    @Schema(description = "Message de salutation", example = "Hello from ReboisTN Event Service!")
    private String message;

    public HelloResponse() {
    }

    public HelloResponse(String service, String status, String message) {
        this.service = service;
        this.status = status;
        this.message = message;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
