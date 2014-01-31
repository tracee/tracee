package de.holisticon.util.tracee.errorlogger.json.beans.values;

/**
 * Value class for JSON generation.
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */
public class ServletRequestAttribute extends NameValuePair{

    @SuppressWarnings("unused")
    private ServletRequestAttribute() {
        this(null, null);
    }

    public ServletRequestAttribute(String name, String value) {
        super(name, value);
    }

}
