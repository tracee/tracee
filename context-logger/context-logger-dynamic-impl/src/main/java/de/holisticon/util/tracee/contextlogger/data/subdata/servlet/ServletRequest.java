package de.holisticon.util.tracee.contextlogger.data.subdata.servlet;

import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.data.Order;
import de.holisticon.util.tracee.contextlogger.data.subdata.NameValuePair;
import de.holisticon.util.tracee.contextlogger.profile.ProfilePropertyNames;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Context provider for HttpServletRequest.
 * Created by Tobias Gindler, holisticon AG on 17.03.14.
 */

@TraceeContextLogProvider(displayName = "request")
public class ServletRequest {

    public static final String ATTR_URL = "url";
    public static final String ATTR_HTTP_METHOD = "http-method";
    public static final String ATTR_HTTP_PARAMETERS = "http-parameters";
    public static final String ATTR_HTTP_REQUEST_HEADERS = "http-request-headers";
    public static final String ATTR_COOKIES = "cookies";

    public static final String ATTR_HTTP_REMOTE_ADDRESS = "http-remote-address";
    public static final String ATTR_HTTP_REMOTE_HOST = "http-remote-host";
    public static final String ATTR_HTTP_REMOTE_PORT = "http-remote-port";

    public static final String ATTR_SCHEMA = "scheme";
    public static final String ATTR_IS_SECURE = "is-secure";
    public static final String ATTR_CONTENT_TYPE = "content-type";
    public static final String ATTR_CONTENT_LENGTH = "content-length";
    public static final String ATTR_LOCALE = "locale";

    public static final int ORDER_URL = 10;
    public static final int ORDER_HTTP_METHOD = 20;
    public static final int ORDER_HTTP_PARAMETERS = 30;
    public static final int ORDER_HTTP_REQUEST_HEADERS = 40;
    public static final int ORDER_ENHANCED_REQUEST_INFO = 60;
    public static final int ORDER_COOKIES = 70;

    // Remote Info
    public static final int ORDER_HTTP_REMOTE_ADDRESS = 150;
    public static final int ORDER_HTTP_REMOTE_HOST = 160;
    public static final int ORDER_HTTP_REMOTE_PORT = 170;

    public static final int ORDER_SCHEMA = 140;
    public static final int ORDER_IS_SECURE = 142;
    public static final int ORDER_CONTENT_TYPE = 144;
    public static final int ORDER_CONTENT_LENGTH = 146;
    public static final int ORDER_LOCALE = 148;

    final HttpServletRequest request;

    public ServletRequest(final HttpServletRequest request
    ) {
        this.request = request;
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "url",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_URL ,
            order = ORDER_URL)
    public String getUrl() {
        return request.getRequestURL().toString();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "http-method",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_HTTP_METHOD ,
            order = ORDER_HTTP_METHOD)
    public String getHttpMethod() {
        return this.request.getMethod();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "http-request-headers",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_HTTP_HEADER ,
            order = ORDER_HTTP_REQUEST_HEADERS)
    public List<NameValuePair> getHttpRequestHeaders() {

        final List<NameValuePair> list = new ArrayList<NameValuePair>();

        final Enumeration<String> httpHeaderNamesEnum = this.request.getHeaderNames();
        while (httpHeaderNamesEnum.hasMoreElements()) {

            final String httpHeaderName = httpHeaderNamesEnum.nextElement();
            final String value =  this.request.getHeader(httpHeaderName);
            list.add(new NameValuePair(httpHeaderName, value));

        }

        return list;
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "http-parameters",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_PARAMETERS ,
            order = ORDER_HTTP_PARAMETERS)
    public List<NameValuePair> getHttpParameters() {

        final List<NameValuePair> list = new ArrayList<NameValuePair>();

        final Enumeration<String> httpHeaderNamesEnum = this.request.getParameterNames();
        while (httpHeaderNamesEnum.hasMoreElements()) {

            final String httpHeaderName = httpHeaderNamesEnum.nextElement();

            final String[] values = this.request.getParameterValues(httpHeaderName);
            if (values != null) {
                for (final String value : values) {
                    list.add(new NameValuePair(httpHeaderName, value));
                }
            }

        }

        return list;

    }



    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(displayName = "cookies", propertyName = ProfilePropertyNames.SERVLET_REQUEST_COOKIES , order = ORDER_COOKIES)
    public List<ServletCookie> getCookies() {

        List<ServletCookie> wrappedCookies = new ArrayList<ServletCookie>();

        for (Cookie cookie : request.getCookies()){
            wrappedCookies.add(new ServletCookie(cookie));
        }

        return wrappedCookies;
    }


    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "http-remote-address",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_REMOTE_ADDRESS ,
            order = ORDER_HTTP_REMOTE_ADDRESS)
    public String getHttpRemoteAddress() {
        return request.getRemoteAddr();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "http-remote-host",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_REMOTE_HOST ,
            order = ORDER_HTTP_REMOTE_HOST)
    public String getHttpRemoteHost() {
        return this.request.getRemoteHost();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "http-remote-port",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_REMOTE_PORT ,
            order = ORDER_HTTP_REMOTE_PORT)
    public Integer getHttpRemotePort() {
        return this.request.getRemotePort();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "scheme",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_SCHEME ,
            order = ORDER_SCHEMA)
    public String getScheme() {
        return this.request.getScheme();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "is-secure",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_IS_SECURE ,
            order = ORDER_IS_SECURE)
    public Boolean getSecure() {
        return this.request.isSecure();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "content-type",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_CONTENT_TYPE ,
            order = ORDER_CONTENT_TYPE)
    public String getContentType() {
        return this.request.getContentType();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "content-length",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_CONTENT_LENGTH,
            order = ORDER_CONTENT_LENGTH)
    public Integer getContentLength() {
        return this.request.getContentLength();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "locale",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_LOCALE ,
            order = ORDER_LOCALE)
    public String getLocale() {
        return this.request.getLocale().toString();
    }
}
