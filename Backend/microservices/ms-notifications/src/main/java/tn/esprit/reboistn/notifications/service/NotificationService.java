package tn.esprit.reboistn.notifications.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.reboistn.notifications.dto.NotificationCountResponse;
import tn.esprit.reboistn.notifications.dto.NotificationResponse;
import tn.esprit.reboistn.notifications.entity.Notification;
import tn.esprit.reboistn.notifications.exception.NotificationNotFoundException;
import tn.esprit.reboistn.notifications.repository.NotificationRepository;

import java.util.List;

/**
 * Service CRUD de base pour les notifications.
 * La logique métier avancée (création à partir d'événements RabbitMQ, envoi d'e-mails)
 * sera ajoutée en S7.
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * Récupère toutes les notifications d'un utilisateur, triées par date décroissante.
     */
    public List<NotificationResponse> getNotificationsForUser(String userId) {
        return notificationRepository.findByDestinataireIdOrderByDateCreationDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Compte les notifications non lues d'un utilisateur.
     */
    public NotificationCountResponse countUnread(String userId) {
        long count = notificationRepository.countByDestinataireIdAndLueFalse(userId);
        return new NotificationCountResponse(userId, count);
    }

    /**
     * Marque une notification comme lue.
     */
    public NotificationResponse markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));
        notification.setLue(true);
        return toResponse(notificationRepository.save(notification));
    }

    /**
     * Supprime une notification par son id.
     */
    public void delete(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new NotificationNotFoundException(id);
        }
        notificationRepository.deleteById(id);
    }

    // --- Mapping ---

    private NotificationResponse toResponse(Notification n) {
        return new NotificationResponse(
                n.getId(),
                n.getDestinataireId(),
                n.getType(),
                n.getCanal(),
                n.getTitre(),
                n.getMessage(),
                n.isLue(),
                n.getDateCreation(),
                n.getDateEnvoi(),
                n.getReferenceId()
        );
    }
}
