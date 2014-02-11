package de.holisticon.util.tracee.backend.jbosslogging;

import de.holisticon.util.tracee.ThreadLocalHashSet;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.spi.TraceeBackendProvider;

import java.util.Set;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
class JbossLoggingTraceeBackendProvider implements TraceeBackendProvider {

    private final JbossLoggingMdcLikeAdapter jbossLoggingMdcLikeAdapter = new JbossLoggingMdcLikeAdapter();
    private final ThreadLocal<Set<String>> traceeKeys = new ThreadLocalHashSet<String>();
    private final JbossLoggingTraceeBackend traceeContext = new JbossLoggingTraceeBackend(jbossLoggingMdcLikeAdapter, traceeKeys);

    @Override
    public TraceeBackend provideBackend() {
        return traceeContext;
    }
}
