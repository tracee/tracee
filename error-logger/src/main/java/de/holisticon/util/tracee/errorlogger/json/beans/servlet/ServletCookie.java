package de.holisticon.util.tracee.errorlogger.json.beans.servlet;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import javax.servlet.http.Cookie;

/**
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */

@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
@JsonPropertyOrder(
        value = {
                ServletCookie.ATTR_NAME,
                ServletCookie.ATTR_VALUE,
                ServletCookie.ATTR_DOMAIN,
                ServletCookie.ATTR_PATH,
                ServletCookie.ATTR_SECURE,
                ServletCookie.ATTR_MAXAGE
        }
)
public final class ServletCookie {

    public static final String ATTR_NAME = "name";
    public static final String ATTR_VALUE = "value";
    public static final String ATTR_DOMAIN = "domain";
    public static final String ATTR_PATH = "path";
    public static final String ATTR_SECURE = "secure";
    public static final String ATTR_MAXAGE = "max-age";


    private final String name;
    private final String value;
    private final String domain;
    private final String path;
    private final Boolean secure;
    private final Integer maxAge;

    @SuppressWarnings("unused")
    private ServletCookie() {
        this(null);
    }

    public ServletCookie (Cookie cookie) {

        this.name = cookie.getName();
        this.value = cookie.getValue();
        this.domain = cookie.getDomain();
        this.path = cookie.getPath();
        this.secure = cookie.getSecure();
        this.maxAge = cookie.getMaxAge();

    }

    @SuppressWarnings("unused")
    public Integer getMaxAge() {
        return maxAge;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public String getValue() {
        return value;
    }

    @SuppressWarnings("unused")
    public String getDomain() {
        return domain;
    }

    @SuppressWarnings("unused")
    public String getPath() {
        return path;
    }

    @SuppressWarnings("unused")
    public Boolean getSecure() {
        return secure;
    }
}
