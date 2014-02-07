package de.holisticon.util.tracee.contextlogger.json.beans;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 * Category for json output for exception context specific data.
 * Created by Tobias Gindler, holisticon AG on 19.12.13.
 */
@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
@JsonPropertyOrder(
        value = {ExceptionCategory.ATTR_MESSAGE, ExceptionCategory.ATTR_STACKTRACE}
)
public final class ExceptionCategory {

    public static final String ATTR_MESSAGE = "message";
    public static final String ATTR_STACKTRACE = "stacktrace";

    @JsonProperty(value = ExceptionCategory.ATTR_MESSAGE)
    private final String message;
    @JsonProperty(value = ExceptionCategory.ATTR_STACKTRACE)
    private final String stacktrace;

    @SuppressWarnings("unused")
    private ExceptionCategory() {
        this(null, null);
    }

    public ExceptionCategory(String message, String stacktrace) {
        this.message = message;
        this.stacktrace = stacktrace;
    }

    @SuppressWarnings("unused")
    public String getMessage() {
        return message;
    }

    @SuppressWarnings("unused")
    public String getStacktrace() {
        return stacktrace;
    }
}
