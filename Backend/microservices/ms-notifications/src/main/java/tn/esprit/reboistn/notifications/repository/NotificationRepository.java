package tn.esprit.reboistn.notifications.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.reboistn.notifications.entity.Notification;

import java.util.List;

/**
 * Repository JPA pour les notifications.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Récupère toutes les notifications d'un destinataire, triées par date de création décroissante.
     */
    List<Notification> findByDestinataireIdOrderByDateCreationDesc(String destinataireId);

    /**
     * Compte les notifications non lues d'un destinataire.
     */
    long countByDestinataireIdAndLueFalse(String destinataireId);
}
