package tn.esprit.reboistn.notifications.event;

import java.time.LocalDateTime;

/**
 * Événement reçu lorsqu'un arbre est planté lors d'un événement.
 * Publié par ms-arbres sur l'exchange reboistn.events (routing key : arbre.plante).
 *
 * TODO S7 : ajouter @RabbitListener pour consommer cet événement.
 */
public record ArbrePlanteEvent(
        Long arbreId,
        Long evenementId,
        String espece,
        String planteurId,
        LocalDateTime datePlantation,
        double latitude,
        double longitude
) {
}
