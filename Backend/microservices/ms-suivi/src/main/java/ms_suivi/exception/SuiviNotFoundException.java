package ms_suivi.exception;

public class SuiviNotFoundException extends RuntimeException {

    public SuiviNotFoundException(Long id) {
        super("Suivi not found with id: " + id);
    }
}
