package ms_inscriptions.exception;

public class InscriptionDuplicateException extends RuntimeException {

	public InscriptionDuplicateException(Long identifiantBenevole, Long identifiantEvenement) {
		super("Le bénévole " + identifiantBenevole
				+ " possède déjà une inscription pour l'événement " + identifiantEvenement + ".");
	}
}
