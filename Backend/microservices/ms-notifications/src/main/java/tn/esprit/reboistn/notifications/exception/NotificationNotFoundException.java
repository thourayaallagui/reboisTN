package tn.esprit.reboistn.notifications.exception;

/**
 * Exception levée lorsqu'une notification n'est pas trouvée.
 */
public class NotificationNotFoundException extends RuntimeException {

    public NotificationNotFoundException(Long id) {
        super("Notification introuvable avec l'id : " + id);
    }
}
