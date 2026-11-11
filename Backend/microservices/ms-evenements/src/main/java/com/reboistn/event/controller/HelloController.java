package com.reboistn.event.controller;

import com.reboistn.event.dto.HelloResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Hello", description = "Endpoint de salutation et vérification de base du service")
public class HelloController {

    @GetMapping("/hello")
    @Operation(
            summary = "Salutation du microservice",
            description = "Retourne l'état de fonctionnement basique du microservice Event Service"
    )
    @ApiResponse(responseCode = "200", description = "Service opérationnel")
    public ResponseEntity<HelloResponse> getHello() {
        HelloResponse response = new HelloResponse(
                "event-service",
                "UP",
                "Hello from ReboisTN Event Service!"
        );
        return ResponseEntity.ok(response);
    }
}
