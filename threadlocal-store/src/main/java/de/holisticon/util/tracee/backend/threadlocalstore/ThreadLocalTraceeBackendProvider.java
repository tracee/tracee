package de.holisticon.util.tracee.backend.threadlocalstore;

import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.spi.TraceeBackendProvider;

/**
 * @author Daniel
 */
public class ThreadLocalTraceeBackendProvider implements TraceeBackendProvider {

    private static final ThreadLocalMap THREAD_LOCAL_MAP = new ThreadLocalMap();

    private final TraceeBackend traceeBackend = new ThreadLocalTraceeBackend(THREAD_LOCAL_MAP);

    @Override
    public final TraceeBackend provideBackend() {
        return traceeBackend;
    }


}
