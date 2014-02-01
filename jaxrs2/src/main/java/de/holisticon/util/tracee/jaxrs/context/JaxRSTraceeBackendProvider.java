package de.holisticon.util.tracee.jaxrs.context;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
@Provider
public class JaxRSTraceeBackendProvider implements ContextResolver<TraceeBackend> {

    @Override
    public TraceeBackend getContext(Class<?> type) {
        return Tracee.getBackend();
    }

}
