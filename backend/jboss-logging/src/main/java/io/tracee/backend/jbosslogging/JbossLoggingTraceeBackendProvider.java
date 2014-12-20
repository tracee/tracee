package io.tracee.backend.jbosslogging;

import io.tracee.ThreadLocalHashSet;
import io.tracee.TraceeBackend;
import io.tracee.spi.TraceeBackendProvider;

import java.util.Set;

public final class JbossLoggingTraceeBackendProvider implements TraceeBackendProvider {

	private static final ThreadLocalHashSet<String> TRACEE_KEYS = new ThreadLocalHashSet<String>();

    private final JbossLoggingTraceeBackend traceeContext = new JbossLoggingTraceeBackend(TRACEE_KEYS);

    @Override
    public TraceeBackend provideBackend() {
        return traceeContext;
    }
}
