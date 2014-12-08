package io.tracee.backend.jbosslogging;

import io.tracee.ThreadLocalHashSet;
import io.tracee.TraceeBackend;
import io.tracee.spi.TraceeBackendProvider;

import java.util.Set;

public final class JbossLoggingTraceeBackendProvider implements TraceeBackendProvider {

    private final JbossLoggingMdcLikeAdapter jbossLoggingMdcLikeAdapter = new JbossLoggingMdcLikeAdapter();
    private final ThreadLocal<Set<String>> traceeKeys = new ThreadLocalHashSet<String>();
    private final JbossLoggingTraceeBackend traceeContext = new JbossLoggingTraceeBackend(jbossLoggingMdcLikeAdapter, traceeKeys);

    @Override
    public TraceeBackend provideBackend() {
        return traceeContext;
    }
}
