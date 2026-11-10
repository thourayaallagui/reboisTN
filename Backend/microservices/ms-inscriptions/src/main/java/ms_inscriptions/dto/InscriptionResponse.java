package ms_inscriptions.dto;

import ms_inscriptions.entity.StatutInscription;

import java.time.LocalDateTime;

/** Données renvoyées lors de la consultation d'une inscription. */
public record InscriptionResponse(
		Long id,
		LocalDateTime dateInscription,
		StatutInscription statut,
		Long identifiantBenevole,
		Long identifiantEvenement,
		boolean presence
) {
}
