package de.holisticon.util.tracee;

/**
 * Exception that indicates a initialization issue with TracEE.
 *
 * @author Daniel Wegener (Holisticon AG)
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

}
