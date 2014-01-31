package de.holisticon.util.tracee.errorlogger.json.beans.values;

/**
 * Value class for JSON generation.
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */
public final class ServletSessionAttribute extends NameValuePair{

    @SuppressWarnings("unused")
    private ServletSessionAttribute() {
        this(null, null);
    }

    public ServletSessionAttribute(String name, String value) {
        super(name, value);
    }

}
