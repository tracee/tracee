package de.holisticon.util.tracee.spi;

import de.holisticon.util.tracee.TraceeBackend;

/**
 * @author Daniel
 */
public interface TraceeBackendProvider {

    public TraceeBackend provideBackend();

}
