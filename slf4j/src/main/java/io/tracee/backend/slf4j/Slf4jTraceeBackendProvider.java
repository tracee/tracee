package io.tracee.backend.slf4j;

import io.tracee.ThreadLocalHashSet;
import io.tracee.TraceeBackend;
import io.tracee.spi.TraceeBackendProvider;

public class Slf4jTraceeBackendProvider implements TraceeBackendProvider {

    private final Slf4jMdcAdapter slf4jMdcAdapter = new Slf4jMdcAdapter();
    private final ThreadLocalHashSet<String> traceeKeys = new ThreadLocalHashSet<String>();
    private final Slf4jTraceeBackend slf4jTraceeContext = new Slf4jTraceeBackend(slf4jMdcAdapter, traceeKeys);

    @Override
    public final TraceeBackend provideBackend() {
        return slf4jTraceeContext;
    }
}
