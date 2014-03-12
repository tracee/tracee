package de.holisticon.util.tracee.contextlogger.json.beans;

/**
 * Created by Tobias Gindler, holisticon AG on 19.12.13.
 */

import com.google.gson.annotations.SerializedName;
import de.holisticon.util.tracee.contextlogger.json.beans.servlet.ServletRequestSubCategory;
import de.holisticon.util.tracee.contextlogger.json.beans.servlet.ServletResponseSubCategory;
import de.holisticon.util.tracee.contextlogger.json.beans.servlet.ServletSessionSubCategory;
import de.holisticon.util.tracee.contextlogger.json.beans.values.ServletRequestAttribute;

import java.util.List;


/**
 * Category for json output for servlet context specific data.
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */
public final class ServletCategory {

    public static final String ATTR_REQUEST = "request";
    public static final String ATTR_RESPONSE = "response";
    public static final String ATTR_SESSION = "session";
    public static final String ATTR_REQUEST_ATTRIBUTES = "request-attributes";

    @SerializedName(ServletCategory.ATTR_REQUEST)
    private final ServletRequestSubCategory request;

    @SerializedName(ServletCategory.ATTR_RESPONSE)
    private final ServletResponseSubCategory response;

    @SerializedName(ServletCategory.ATTR_SESSION)
    private final ServletSessionSubCategory session;

    @SerializedName(ServletCategory.ATTR_REQUEST_ATTRIBUTES)
    private final List<ServletRequestAttribute> requestAttributes;

    @SuppressWarnings("unused")
    private ServletCategory() {
        this(null, null, null, null);
    }

    public ServletCategory(
            ServletRequestSubCategory request,
            ServletResponseSubCategory response,
            ServletSessionSubCategory session,
            List<ServletRequestAttribute> requestAttributes
    ) {

        this.request = request;
        this.response = response;
        this.session = session;
        this.requestAttributes = requestAttributes;

    }

    @SuppressWarnings("unused")
    public ServletRequestSubCategory getRequest() {
        return request;
    }

    @SuppressWarnings("unused")
    public ServletResponseSubCategory getResponse() {
        return response;
    }

    @SuppressWarnings("unused")
    public ServletSessionSubCategory getSession() {
        return session;
    }

    @SuppressWarnings("unused")
    public List<ServletRequestAttribute> getRequestAttributes() {
        return requestAttributes;
    }
}


