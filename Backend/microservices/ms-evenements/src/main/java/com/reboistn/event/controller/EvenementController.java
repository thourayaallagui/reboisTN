package com.reboistn.event.controller;

import com.reboistn.event.dto.EvenementRequest;
import com.reboistn.event.dto.EvenementResponse;
import com.reboistn.event.enums.StatutEvenement;
import com.reboistn.event.enums.TypeEvenement;
import com.reboistn.event.service.EvenementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/events", "/api/evenements"})
@Tag(name = "Événements", description = "Gestion du cycle de vie des événements écologiques de ReboisTN")
public class EvenementController {

    private final EvenementService evenementService;

    public EvenementController(EvenementService evenementService) {
        this.evenementService = evenementService;
    }

    @PostMapping
    @Operation(summary = "Créer un nouvel événement", description = "Crée un événement avec statut initial BROUILLON et 0 inscrits.")
    @ApiResponse(responseCode = "201", description = "Événement créé avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides ou non respect des règles métier")
    public ResponseEntity<EvenementResponse> createEvenement(@Valid @RequestBody EvenementRequest request) {
        EvenementResponse response = evenementService.createEvenement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Lister les événements", description = "Récupère les événements avec filtres optionnels par type, statut et ville.")
    @ApiResponse(responseCode = "200", description = "Liste des événements récupérée avec succès")
    public ResponseEntity<List<EvenementResponse>> getAllEvenements(
            @Parameter(description = "Filtrer par type d'événement")
            @RequestParam(required = false) TypeEvenement type,
            @Parameter(description = "Filtrer par statut")
            @RequestParam(required = false) StatutEvenement statut,
            @Parameter(description = "Filtrer par ville")
            @RequestParam(required = false) String ville) {
        List<EvenementResponse> list = evenementService.getAllEvenements(type, statut, ville);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un événement par son ID")
    @ApiResponse(responseCode = "200", description = "Événement trouvé")
    @ApiResponse(responseCode = "404", description = "Événement non trouvé")
    public ResponseEntity<EvenementResponse> getEvenementById(@PathVariable Long id) {
        EvenementResponse response = evenementService.getEvenementById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un événement existant")
    @ApiResponse(responseCode = "200", description = "Événement modifié")
    @ApiResponse(responseCode = "400", description = "Données invalides ou règle métier enfreinte")
    @ApiResponse(responseCode = "404", description = "Événement non trouvé")
    public ResponseEntity<EvenementResponse> updateEvenement(
            @PathVariable Long id,
            @Valid @RequestBody EvenementRequest request) {
        EvenementResponse response = evenementService.updateEvenement(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un événement")
    @ApiResponse(responseCode = "204", description = "Événement supprimé")
    @ApiResponse(responseCode = "404", description = "Événement non trouvé")
    public ResponseEntity<Void> deleteEvenement(@PathVariable Long id) {
        evenementService.deleteEvenement(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/statut")
    @Operation(summary = "Mettre à jour le statut d'un événement", description = "Permet de publier, terminer ou annuler un événement. Règle : un événement ANNULE ne peut plus être PUBLIE.")
    @ApiResponse(responseCode = "200", description = "Statut mis à jour")
    @ApiResponse(responseCode = "400", description = "Transition de statut interdite")
    @ApiResponse(responseCode = "404", description = "Événement non trouvé")
    public ResponseEntity<EvenementResponse> updateStatut(
            @PathVariable Long id,
            @RequestParam StatutEvenement nouveauStatut) {
        EvenementResponse response = evenementService.updateStatut(id, nouveauStatut);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/inscriptions")
    @Operation(summary = "Inscrire un participant", description = "Incrémente le nombre d'inscrits en vérifiant que l'événement n'est ni terminé, ni annulé, ni complet.")
    @ApiResponse(responseCode = "200", description = "Inscription prise en compte")
    @ApiResponse(responseCode = "400", description = "Événement complet, annulé ou terminé")
    public ResponseEntity<EvenementResponse> inscrire(@PathVariable Long id) {
        EvenementResponse response = evenementService.inscrire(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/inscriptions")
    @Operation(summary = "Annuler une inscription", description = "Décrémente le nombre d'inscrits.")
    @ApiResponse(responseCode = "200", description = "Désinscription prise en compte")
    @ApiResponse(responseCode = "400", description = "Nombre d'inscrits déjà à 0")
    public ResponseEntity<EvenementResponse> desinscrire(@PathVariable Long id) {
        EvenementResponse response = evenementService.desinscrire(id);
        return ResponseEntity.ok(response);
    }
}
