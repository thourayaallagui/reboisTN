package tn.esprit.reboistn.notifications.event;

import java.time.LocalDateTime;

/**
 * Événement reçu lorsqu'un nouvel événement de plantation est créé.
 * Publié par ms-evenements sur l'exchange reboistn.events (routing key : evenement.cree).
 *
 * TODO S7 : ajouter @RabbitListener pour consommer cet événement.
 */
public record EvenementCreeEvent(
        Long evenementId,
        String titre,
        String description,
        LocalDateTime dateDebut,
        String organisateurId,
        String lieu
) {
}
