package de.holisticon.util.tracee.errorlogger.json.beans.values;

/**
 * Created by Tobias Gindler, holisticon AG on 20.12.13.
 */
public final class TraceeContextValue extends NameValuePair {

    @SuppressWarnings("unused")
    private TraceeContextValue() {
        this(null, null);
    }

    public TraceeContextValue(String name, String value) {
        super(name, value);
    }

}
