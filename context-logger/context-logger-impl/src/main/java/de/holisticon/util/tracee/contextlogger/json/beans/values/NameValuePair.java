package de.holisticon.util.tracee.contextlogger.json.beans.values;

import com.google.gson.annotations.SerializedName;

/**
 * Abstract value class for JSON generation.
 * Created by Tobias Gindler on 20.12.13.
 */
public class NameValuePair {

    public static final String ATTR_NAME = "name";
    public static final String ATTR_VALUE = "value";

    @SerializedName(value = NameValuePair.ATTR_NAME)
    private final String name;

    @SerializedName(value = NameValuePair.ATTR_VALUE)
    private final String value;

    @SuppressWarnings("unused")
    private NameValuePair() {
        this(null, null);
    }

    public NameValuePair(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public final String getName() {
        return name;
    }

    public final String getValue() {
        return value;
    }
}
