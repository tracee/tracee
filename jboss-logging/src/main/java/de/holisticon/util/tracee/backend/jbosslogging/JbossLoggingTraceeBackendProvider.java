package de.holisticon.util.tracee.backend.jbosslogging;

import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.spi.TraceeBackendProvider;

/**
 * @author Daniel
 */
class JbossLoggingTraceeBackendProvider implements TraceeBackendProvider {

    private final JbossLoggingTraceeBackend traceeContext = new JbossLoggingTraceeBackend();

    @Override
    public TraceeBackend provideBackend() {
        return traceeContext;
    }
}
