package de.holisticon.util.tracee.contextlogger.json.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Category for json output for exception context specific data.
 * Created by Tobias Gindler, holisticon AG on 19.12.13.
 */
public final class ExceptionCategory {

    public static final String ATTR_MESSAGE = "message";
    public static final String ATTR_STACKTRACE = "stacktrace";

    @SerializedName(ExceptionCategory.ATTR_MESSAGE)
    private final String message;
    @SerializedName(ExceptionCategory.ATTR_STACKTRACE)
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
