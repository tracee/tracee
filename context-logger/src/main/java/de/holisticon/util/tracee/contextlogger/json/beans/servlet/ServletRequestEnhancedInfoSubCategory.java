package de.holisticon.util.tracee.contextlogger.json.beans.servlet;

import com.google.gson.annotations.SerializedName;

/**
 * HttpServletRequest context specific class for  JSON generation.
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */

public class ServletRequestEnhancedInfoSubCategory {

    public static final String ATTR_SCHEMA = "scheme";
    public static final String ATTR_IS_SECURE = "is-secure";
    public static final String ATTR_CONTENT_TYPE = "content-type";
    public static final String ATTR_CONTENT_LENGTH = "content-length";
    public static final String ATTR_LOCALE = "locale";

    @SerializedName(ServletRequestEnhancedInfoSubCategory.ATTR_SCHEMA)
    private final String scheme;
    @SerializedName(ServletRequestEnhancedInfoSubCategory.ATTR_IS_SECURE)
    private final Boolean secure;
    @SerializedName(ServletRequestEnhancedInfoSubCategory.ATTR_CONTENT_TYPE)
    private final String contentType;
    @SerializedName(ServletRequestEnhancedInfoSubCategory.ATTR_CONTENT_LENGTH)
    private final Integer contentLength;
    @SerializedName(ServletRequestEnhancedInfoSubCategory.ATTR_LOCALE)
    private final String locale;

    @SuppressWarnings("unused")
    private ServletRequestEnhancedInfoSubCategory() {
        this(null, null, null, null, null);
    }

    public ServletRequestEnhancedInfoSubCategory(
        String scheme,
        Boolean secure,
        String contentType,
        Integer contentLength,
        String locale) {
        this.scheme = scheme;
        this.secure = secure;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.locale = locale;
    }

    @SuppressWarnings("unused")
    public String getScheme() {
        return scheme;
    }

    @SuppressWarnings("unused")
    public Boolean getSecure() {
        return secure;
    }

    @SuppressWarnings("unused")
    public String getContentType() {
        return contentType;
    }

    @SuppressWarnings("unused")
    public Integer getContentLength() {
        return contentLength;
    }

    @SuppressWarnings("unused")
    public String getLocale() {
        return locale;
    }
}
