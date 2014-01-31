package de.holisticon.util.tracee.backend.log4j;

import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.spi.TraceeBackendProvider;

/**
 * @author Daniel
 */
public class Log4jTraceeBackendProvider implements TraceeBackendProvider {

    private final Log4jTraceeBackend slf4jTraceeContext = new Log4jTraceeBackend();

    @Override
    public final TraceeBackend provideBackend() {
        return slf4jTraceeContext;
    }
}
