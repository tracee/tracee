package io.tracee.contextlogger.api;

import io.tracee.contextlogger.data.subdata.tracee.PassedDataContextProvider;

import java.util.Set;

/**
 * Annotation to mark class as toJson builder implementations.
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */
public interface TraceeContextLogBuilder {

    Set<Class> getWrapperClasses();

    void setWrapperClasses(final Set<Class> wrapperClasses);

    String log(final Object... instancesToLog);

    String logPassedContext(final PassedDataContextProvider passedContextData);

}
