package de.holisticon.util.tracee.contextlogger.json.beans.values;

/**
 * Value class for JSON generation.
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
