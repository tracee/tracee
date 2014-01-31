package de.holisticon.util.tracee.errorlogger.json.beans.servlet;

import de.holisticon.util.tracee.errorlogger.json.beans.values.ServletSessionAttribute;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.util.List;

/**
 * Session context specific class for  JSON generation.
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */

@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
@JsonPropertyOrder(
        value = {
                ServletSessionSubCategory.ATTR_SESSION_EXISTS,
                ServletSessionSubCategory.ATTR_USER_NAME,
                ServletSessionSubCategory.ATTR_SESSION_ATTRIBUTES
        }
)
public final class ServletSessionSubCategory {

    public static final String ATTR_SESSION_EXISTS = "session-exists";
    public static final String ATTR_USER_NAME = "user-name";
    public static final String ATTR_SESSION_ATTRIBUTES = "attributes";

    private final Boolean sessionExists;
    private final String userName;
    private final List<ServletSessionAttribute> sessionAttributes;



    @SuppressWarnings("unused")
    private ServletSessionSubCategory() {
        this(null, null, null);
    }

    public ServletSessionSubCategory (
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
