package io.tracee;

/**
 * Exception that indicates a initialization issue with TracEE.
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
