package tn.esprit.reboistn.notifications.dto;

/**
 * DTO pour le comptage des notifications non lues.
 */
public record NotificationCountResponse(
        String destinataireId,
        long count
) {
}
