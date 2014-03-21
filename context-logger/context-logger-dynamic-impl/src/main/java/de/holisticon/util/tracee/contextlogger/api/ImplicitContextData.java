package de.holisticon.util.tracee.contextlogger.api;

import de.holisticon.util.tracee.contextlogger.ImplicitContext;

/**
 * Interface to mark a class that provides implicit context data.(Class doesn't need external context information)
 * Created by Tobias Gindler, holisticon AG on 21.03.14.
 */
public interface ImplicitContextData {

    /**
     * Gets the implict context type
     *
     * @return
     */
    ImplicitContext getImplicitContext();

}
