package ms_inscriptions.exception;

public class PresenceInvalideException extends RuntimeException {

	public PresenceInvalideException(Long id) {
		super("La présence ne peut être enregistrée que pour une inscription confirmée (id=" + id + ").");
	}
}
