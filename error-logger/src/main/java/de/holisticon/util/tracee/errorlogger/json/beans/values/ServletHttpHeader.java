package de.holisticon.util.tracee.errorlogger.json.beans.values;

/**
 * Created by Tobias Gindler, holisticon AG on 20.12.13.
 */
public final class ServletHttpHeader extends NameValuePair {

    @SuppressWarnings("unused")
    private ServletHttpHeader() {
        this(null, null);
    }

    public ServletHttpHeader(String name, String value) {
        super(name, value);
    }


}
