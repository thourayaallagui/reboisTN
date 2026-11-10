package ms_inscriptions.exception;

public class InscriptionNotFoundException extends RuntimeException {

	public InscriptionNotFoundException(Long id) {
		super("Aucune inscription trouvée avec l'identifiant " + id + ".");
	}
}
