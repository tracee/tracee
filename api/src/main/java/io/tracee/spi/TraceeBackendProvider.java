package io.tracee.spi;

import io.tracee.TraceeBackend;

/**
 * @author Daniel
 */
public interface TraceeBackendProvider {

    TraceeBackend provideBackend();

}
