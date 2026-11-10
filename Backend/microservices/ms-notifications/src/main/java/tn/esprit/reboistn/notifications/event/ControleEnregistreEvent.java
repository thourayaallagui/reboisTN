package tn.esprit.reboistn.notifications.event;

import java.time.LocalDateTime;

/**
 * Événement reçu lorsqu'un contrôle de survie est enregistré pour un arbre.
 * Publié par ms-suivi sur l'exchange reboistn.events (routing key : controle.enregistre).
 *
 * TODO S7 : ajouter @RabbitListener pour consommer cet événement.
 */
public record ControleEnregistreEvent(
        Long controleId,
        Long arbreId,
        String etatSante,
        double hauteur,
        String commentaire,
        LocalDateTime dateControle
) {
}
