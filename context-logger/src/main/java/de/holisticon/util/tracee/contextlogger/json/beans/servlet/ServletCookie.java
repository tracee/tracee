package de.holisticon.util.tracee.contextlogger.json.beans.servlet;

import com.google.gson.annotations.SerializedName;

import javax.servlet.http.Cookie;

/**
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */
public final class ServletCookie {

    public static final String ATTR_NAME = "name";
    public static final String ATTR_VALUE = "value";
    public static final String ATTR_DOMAIN = "domain";
    public static final String ATTR_PATH = "path";
    public static final String ATTR_SECURE = "secure";
    public static final String ATTR_MAXAGE = "max-age";

	@SerializedName(ATTR_NAME)
    private final String name;
	@SerializedName(ATTR_VALUE)
    private final String value;
	@SerializedName(ATTR_DOMAIN)
	private final String domain;
	@SerializedName(ATTR_PATH)
    private final String path;
	@SerializedName(ATTR_SECURE)
    private final Boolean secure;
	@SerializedName(ATTR_MAXAGE)
    private final Integer maxAge;

    @SuppressWarnings("unused")
    private ServletCookie() {
        this(null);
    }

    public ServletCookie(Cookie cookie) {

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
