package system.dev.marques.exception.custom;

public class EmailExistingException extends RuntimeException {
    public EmailExistingException(String message) {
        super(message);
    }
}
