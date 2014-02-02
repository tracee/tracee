package de.holisticon.util.tracee.transport;

import de.holisticon.util.tracee.TraceeBackend;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public interface TransportSerialization<T> {

    void mergeToBackend(TraceeBackend backend, T serialized);
    T render(TraceeBackend backend);

}
