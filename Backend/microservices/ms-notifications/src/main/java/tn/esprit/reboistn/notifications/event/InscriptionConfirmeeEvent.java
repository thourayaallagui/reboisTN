package tn.esprit.reboistn.notifications.event;

import java.time.LocalDateTime;

/**
 * Événement reçu lorsqu'une inscription à un événement est confirmée.
 * Publié par ms-inscriptions sur l'exchange reboistn.events (routing key : inscription.confirmee).
 *
 * TODO S7 : ajouter @RabbitListener pour consommer cet événement.
 */
public record InscriptionConfirmeeEvent(
        Long inscriptionId,
        Long evenementId,
        String participantId,
        String titreEvenement,
        LocalDateTime dateInscription
) {
}
