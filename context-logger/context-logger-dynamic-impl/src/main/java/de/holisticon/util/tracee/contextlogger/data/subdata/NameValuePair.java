package de.holisticon.util.tracee.contextlogger.data.subdata;

import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;

/**
 * Main Interface for name value pairs.
 * Created by Tobias Gindler on 21.03.14.
 */
@TraceeContextLogProvider(displayName = "name-value-pair")
public abstract class NameValuePair<T> {

    public static final String ATTR_NAME = "name";
    public static final String ATTR_VALUE = "value";

    private final String name;
    private final T value;

    public NameValuePair (final String name, final T value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Gets the name for the value.
     *
     * @return the name for the value
     */
    @TraceeContextLogProviderMethod(displayName = "name", propertyName = "", order=1)
    public String getName() {
        return this.name;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    @TraceeContextLogProviderMethod(displayName = "value", propertyName = "", order=2)
    public T getValue() {
        return this.value;
    }

}
