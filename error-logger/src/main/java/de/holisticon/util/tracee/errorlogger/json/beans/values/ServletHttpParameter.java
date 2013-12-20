package de.holisticon.util.tracee.errorlogger.json.beans.values;

/**
 * Created by Tobias Gindler, holisticon on 20.12.13.
 */
public final class ServletHttpParameter extends NameValuePair {

    @SuppressWarnings("unused")
    private ServletHttpParameter() {
        this(null, null);
    }

    public ServletHttpParameter(String name, String value) {
        super(name, value);
    }

}
