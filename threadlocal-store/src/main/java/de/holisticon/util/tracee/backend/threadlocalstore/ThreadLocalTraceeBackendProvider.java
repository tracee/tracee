package de.holisticon.util.tracee.backend.threadlocalstore;

import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.spi.TraceeBackendProvider;

/**
 * @author Daniel
 */
public class ThreadLocalTraceeBackendProvider implements TraceeBackendProvider {

    private static final ThreadLocalMap threadLocalMap = new ThreadLocalMap();

    private final TraceeBackend traceeBackend = new ThreadLocalTraceeBackend(threadLocalMap);

    @Override
    public TraceeBackend provideBackend() {
        return traceeBackend;
    }


}
