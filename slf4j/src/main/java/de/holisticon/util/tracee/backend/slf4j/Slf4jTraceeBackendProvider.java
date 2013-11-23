package de.holisticon.util.tracee.backend.slf4j;

import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.spi.TraceeBackendProvider;

/**
 * @author Daniel
 */
public class Slf4jTraceeBackendProvider implements TraceeBackendProvider {

    private final Slf4jTraceeBackend slf4jTraceeContext = new Slf4jTraceeBackend();

    @Override
    public final TraceeBackend provideBackend() {
        return slf4jTraceeContext;
    }
}
