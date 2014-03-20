package de.holisticon.util.tracee.contextlogger.api;

/**
 * Interface that marks a class and provides a method to set the context information.
 * Created by Tobias Gindler on 20.03.14.
 */
public interface WrappedContextData <T> {
    void setContextData(Object instance);
    T getContextData();
}
