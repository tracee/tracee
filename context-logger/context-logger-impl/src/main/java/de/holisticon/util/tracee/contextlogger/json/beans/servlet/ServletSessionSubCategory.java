package de.holisticon.util.tracee.contextlogger.json.beans.servlet;

import com.google.gson.annotations.SerializedName;
import de.holisticon.util.tracee.contextlogger.json.beans.values.ServletSessionAttribute;

import java.util.List;

/**
 * Session context specific class for  JSON generation.
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */

public final class ServletSessionSubCategory {

    public static final String ATTR_SESSION_EXISTS = "session-exists";
    public static final String ATTR_USER_NAME = "user-name";
    public static final String ATTR_SESSION_ATTRIBUTES = "attributes";

	@SerializedName(ATTR_SESSION_EXISTS)
    private final Boolean sessionExists;
	@SerializedName(ATTR_USER_NAME)
    private final String userName;
	@SerializedName(ATTR_SESSION_ATTRIBUTES)
    private final List<ServletSessionAttribute> sessionAttributes;

    @SuppressWarnings("unused")
    private ServletSessionSubCategory() {
        this(null, null, null);
    }

    public ServletSessionSubCategory(
            Boolean sessionExists,
            String userName,
            List<ServletSessionAttribute> sessionAttributes
    ) {

        this.sessionExists = sessionExists;
        this.userName = userName;
        this.sessionAttributes = sessionAttributes;

    }

    @SuppressWarnings("unused")
    public Boolean getSessionExists() {
        return sessionExists;
    }

    @SuppressWarnings("unused")
    public String getUserName() {
        return userName;
    }

    @SuppressWarnings("unused")
    public List<ServletSessionAttribute> getSessionAttributes() {
        return sessionAttributes;
    }
}
