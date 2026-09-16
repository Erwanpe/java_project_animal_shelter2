package exception;

public class ShelterException extends RuntimeException {

    public ShelterException(String message) {
        super(message);
    }

    public ShelterException(String message, Throwable cause) {
        super(message, cause);
    }
}