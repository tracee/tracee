package io.tracee.backend.slf4j;

import io.tracee.ThreadLocalHashSet;
import io.tracee.TraceeBackend;
import io.tracee.spi.TraceeBackendProvider;

public class Slf4jTraceeBackendProvider implements TraceeBackendProvider {

	private static final ThreadLocalHashSet<String> TRACEE_KEYS = new ThreadLocalHashSet<>();

    private final Slf4jTraceeBackend slf4jTraceeContext = new Slf4jTraceeBackend(TRACEE_KEYS);

    @Override
    public final TraceeBackend provideBackend() {
        return slf4jTraceeContext;
    }
}
