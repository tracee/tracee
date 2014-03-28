package de.holisticon.util.tracee.contextlogger.api;

import de.holisticon.util.tracee.contextlogger.data.subdata.tracee.PassedContextDataProvider;

import java.util.Set;

/**
 * Annotation to mark class as toJson builder implementations.
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */
public interface TraceeContextLogBuilder {

    Set<Class> getWrapperClasses();

    void setWrapperClasses(final Set<Class> wrapperClasses);

    String log(final Object... instancesToLog);

    String logPassedContext(final PassedContextDataProvider passedContextData);

}
