package io.tracee.spi;

import io.tracee.TraceeBackend;

public interface TraceeBackendProvider {

    TraceeBackend provideBackend();

}
