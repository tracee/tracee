package de.holisticon.util.tracee.examples.jaxws.service;

/**
 * Created by Tobias Gindler, holisticon AG on 16.01.14.
 */
public class TraceeExampleServiceRuntimeException extends RuntimeException{

    public TraceeExampleServiceRuntimeException (int a, int b) {
        super("Triggered exception with passed parameters '" + a + "' and '" +b + "'");
    }

}
