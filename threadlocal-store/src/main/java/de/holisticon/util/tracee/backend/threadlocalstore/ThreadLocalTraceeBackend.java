package de.holisticon.util.tracee.backend.threadlocalstore;

import de.holisticon.util.tracee.MDCLike;
import de.holisticon.util.tracee.MDCLikeTraceeBackend;
import de.holisticon.util.tracee.ThreadLocalHashSet;
import de.holisticon.util.tracee.TraceeLogger;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
class ThreadLocalTraceeBackend extends MDCLikeTraceeBackend {


    public ThreadLocalTraceeBackend(final MDCLike mdcLike, ThreadLocalHashSet<String> traceeKeys) {
        super(mdcLike, traceeKeys);
    }


    @Override
    public final TraceeLogger getLogger(final Class<?> clazz) {
        return new ThreadLocalTraceeLogger(clazz);
    }



}
