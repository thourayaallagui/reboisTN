package tn.esprit.reboistn.notifications.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.reboistn.notifications.dto.NotificationCountResponse;
import tn.esprit.reboistn.notifications.dto.NotificationResponse;
import tn.esprit.reboistn.notifications.service.NotificationService;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des notifications.
 *
 * L'utilisateur connecté est identifié par l'en-tête temporaire X-User-Id.
 * TODO S8 : remplacer X-User-Id par le "sub" du JWT Keycloak.
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Gestion des notifications utilisateur")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Lister les notifications de l'utilisateur connecté")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Liste des notifications"))
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(notificationService.getNotificationsForUser(userId));
    }

    @GetMapping("/non-lues")
    @Operation(summary = "Nombre de notifications non lues")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Compteur de notifications non lues"))
    public ResponseEntity<NotificationCountResponse> countUnread(
            @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(notificationService.countUnread(userId));
    }

    @PatchMapping("/{id}/lue")
    @Operation(summary = "Marquer une notification comme lue")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notification marquée comme lue"),
            @ApiResponse(responseCode = "404", description = "Notification introuvable")
    })
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une notification")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Notification supprimée"),
            @ApiResponse(responseCode = "404", description = "Notification introuvable")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
