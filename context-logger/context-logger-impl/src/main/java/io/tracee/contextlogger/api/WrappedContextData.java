package io.tracee.contextlogger.api;


/**
 * Interface that marks a class and provides a method to set the context information.
 * Created by Tobias Gindler on 20.03.14.
 */
public interface WrappedContextData<T> {
    /**
     * Used to set context data via reflection.
     *
     * @param instance the context data instance to set
     * @throws ClassCastException if passed instance type is incompatible with wrapper type.
     */
    void setContextData(Object instance) throws ClassCastException;

    /**
     * Used to determine the wrapped type of the class.
     *
     * @return
     */
    Class<T> getWrappedType();
}
