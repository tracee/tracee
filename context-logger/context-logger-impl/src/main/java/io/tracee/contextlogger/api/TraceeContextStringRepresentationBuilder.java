package io.tracee.contextlogger.api;

import io.tracee.contextlogger.contextprovider.tracee.PassedDataContextProvider;

import java.util.Set;

/**
 * Annotation to mark class as toJson builder implementations.
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */
public interface TraceeContextStringRepresentationBuilder {

    Set<Class> getWrapperClasses();

    void setWrapperClasses(final Set<Class> wrapperClasses);

    boolean getKeepOrder();

    void setKeepOrder(final boolean keepOrder);

    String createStringRepresentation(final Object... instancesToLog);

    String createStringRepresentationForPassedDataContextProvider(final PassedDataContextProvider passedContextData);

}
