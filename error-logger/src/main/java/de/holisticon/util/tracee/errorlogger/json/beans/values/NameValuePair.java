package de.holisticon.util.tracee.errorlogger.json.beans.values;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 * Abstract value class for JSON generation.
 * Created by Tobias Gindler on 20.12.13.
 */

@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
@JsonPropertyOrder(value = {NameValuePair.ATTR_NAME,
        NameValuePair.ATTR_VALUE}
)
public class NameValuePair {

    public static final String ATTR_NAME = "name";
    public static final String ATTR_VALUE = "value";

    @JsonProperty(value = NameValuePair.ATTR_NAME)
    private final String name;

    @JsonProperty(value = NameValuePair.ATTR_VALUE)
    private final String value;

    @SuppressWarnings("unused")
    private NameValuePair() {
        // used by jackson
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
