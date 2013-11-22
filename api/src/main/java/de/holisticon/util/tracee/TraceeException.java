package de.holisticon.util.tracee;

/**
 * @author Daniel
 */
public class TraceeException extends RuntimeException {

    public TraceeException() {
    }

    public TraceeException(String message) {
        super(message);
    }

    public TraceeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TraceeException(Throwable cause) {
        super(cause);
    }

    public TraceeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
