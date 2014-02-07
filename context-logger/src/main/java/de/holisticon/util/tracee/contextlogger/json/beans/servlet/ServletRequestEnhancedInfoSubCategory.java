package de.holisticon.util.tracee.contextlogger.json.beans.servlet;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 * HttpServletRequest context specific class for  JSON generation.
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */

@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
@JsonPropertyOrder(
        value = {
                ServletRequestEnhancedInfoSubCategory.ATTR_SCHEMA,
                ServletRequestEnhancedInfoSubCategory.ATTR_IS_SECURE,
                ServletRequestEnhancedInfoSubCategory.ATTR_CONTENT_TYPE,
                ServletRequestEnhancedInfoSubCategory.ATTR_CONTENT_LENGTH,
                ServletRequestEnhancedInfoSubCategory.ATTR_LOCALE
        }
)
public class ServletRequestEnhancedInfoSubCategory {

    public static final String ATTR_SCHEMA = "scheme";
    public static final String ATTR_IS_SECURE = "is-secure";
    public static final String ATTR_CONTENT_TYPE = "content-type";
    public static final String ATTR_CONTENT_LENGTH = "content-length";
    public static final String ATTR_LOCALE = "locale";


    @JsonProperty(value = ServletRequestEnhancedInfoSubCategory.ATTR_SCHEMA)
    private final String scheme;
    @JsonProperty(value = ServletRequestEnhancedInfoSubCategory.ATTR_IS_SECURE)
    private final Boolean secure;
    @JsonProperty(value = ServletRequestEnhancedInfoSubCategory.ATTR_CONTENT_TYPE)
    private final String contentType;
    @JsonProperty(value = ServletRequestEnhancedInfoSubCategory.ATTR_CONTENT_LENGTH)
    private final Integer contentLength;
    @JsonProperty(value = ServletRequestEnhancedInfoSubCategory.ATTR_LOCALE)
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
        String locale

    ) {
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
