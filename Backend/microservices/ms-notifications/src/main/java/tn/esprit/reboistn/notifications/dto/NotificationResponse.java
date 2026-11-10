package tn.esprit.reboistn.notifications.dto;

import tn.esprit.reboistn.notifications.enums.CanalNotification;
import tn.esprit.reboistn.notifications.enums.TypeNotification;

import java.time.LocalDateTime;

/**
 * DTO de réponse pour une notification.
 */
public record NotificationResponse(
        Long id,
        String destinataireId,
        TypeNotification type,
        CanalNotification canal,
        String titre,
        String message,
        boolean lue,
        LocalDateTime dateCreation,
        LocalDateTime dateEnvoi,
        Long referenceId
) {
}
