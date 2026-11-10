package ms_inscriptions.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.stream.Collectors;

/** Traduit les erreurs applicatives en réponses HTTP JSON cohérentes. */
@RestControllerAdvice
public class GestionGlobaleExceptions {

	private static final Logger LOGGER = LoggerFactory.getLogger(GestionGlobaleExceptions.class);

	@ExceptionHandler(InscriptionNotFoundException.class)
	public ResponseEntity<ErreurApi> gererInscriptionIntrouvable(InscriptionNotFoundException exception) {
		return reponse(HttpStatus.NOT_FOUND, exception.getMessage());
	}

	@ExceptionHandler({
			InscriptionDuplicateException.class,
			TransitionStatutInvalideException.class,
			PresenceInvalideException.class
	})
	public ResponseEntity<ErreurApi> gererConflit(RuntimeException exception) {
		return reponse(HttpStatus.CONFLICT, exception.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErreurApi> gererRequeteInvalide(MethodArgumentNotValidException exception) {
		String details = exception.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.collect(Collectors.joining("; "));
		return reponse(HttpStatus.BAD_REQUEST, "Champs invalides : " + details);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErreurApi> gererCorpsIllisible(HttpMessageNotReadableException exception) {
		return reponse(HttpStatus.BAD_REQUEST, "Le corps JSON est illisible ou invalide.");
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErreurApi> gererTypeParametreInvalide(
			MethodArgumentTypeMismatchException exception) {
		return reponse(HttpStatus.BAD_REQUEST,
				"Le paramètre '" + exception.getName() + "' doit avoir un format valide.");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErreurApi> gererErreurInterne(Exception exception) {
		LOGGER.error("Erreur inattendue lors du traitement d'une requête.", exception);
		return reponse(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur interne est survenue.");
	}

	private ResponseEntity<ErreurApi> reponse(HttpStatus status, String message) {
		return ResponseEntity.status(status)
				.body(new ErreurApi(Instant.now(), status.value(), message));
	}

	public record ErreurApi(Instant timestamp, int status, String message) {
	}
}
