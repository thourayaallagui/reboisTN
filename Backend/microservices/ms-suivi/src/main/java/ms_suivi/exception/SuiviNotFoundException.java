package ms_suivi.exception;

public class SuiviNotFoundException extends ResourceNotFoundException {

    public SuiviNotFoundException(Long id) {
        super("Suivi", id);
    }
}
