package de.holisticon.util.tracee.contextlogger.builder;

import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogBuilder;

import java.util.Set;

/**
 * Abstract base class for all context toJson builder implementations.
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */
public abstract class AbstractContextLogBuilder implements TraceeContextLogBuilder{

    private Set<Class> wrapperClasses;

    @Override
    public Set<Class> getWrapperClasses() {
        return wrapperClasses;
    }

    @Override
    public void setWrapperClasses(Set<Class> wrapperClasses) {
        this.wrapperClasses = wrapperClasses;
    }

}
