package ms_inscriptions.dto;

import jakarta.validation.constraints.NotNull;

/** Données requises pour demander une inscription. */
public record InscriptionRequest(
		@NotNull Long identifiantBenevole,
		@NotNull Long identifiantEvenement
) {
}
