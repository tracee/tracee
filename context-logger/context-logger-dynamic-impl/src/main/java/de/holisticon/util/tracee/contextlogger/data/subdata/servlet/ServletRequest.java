package de.holisticon.util.tracee.contextlogger.data.subdata.servlet;

import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.api.WrappedContextData;
import de.holisticon.util.tracee.contextlogger.data.subdata.NameObjectValuePair;
import de.holisticon.util.tracee.contextlogger.data.subdata.NameStringValuePair;
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
public class ServletRequest implements WrappedContextData<HttpServletRequest> {


    HttpServletRequest request;

    public ServletRequest() {

    }

    public ServletRequest(final HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setContextData(Object instance) throws ClassCastException {
        this.request = (HttpServletRequest) instance;
    }

    @Override
    public Class<HttpServletRequest> getWrappedType() {
        return HttpServletRequest.class;
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "url",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_URL,
            order = 10)
    public String getUrl() {
        return request.getRequestURL().toString();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "http-method",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_HTTP_METHOD,
            order = 20)
    public String getHttpMethod() {
        return this.request.getMethod();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "http-parameters",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_PARAMETERS,
            order = 30)
    public List<NameStringValuePair> getHttpParameters() {

        final List<NameStringValuePair> list = new ArrayList<NameStringValuePair>();

        final Enumeration<String> httpHeaderNamesEnum = this.request.getParameterNames();
        while (httpHeaderNamesEnum.hasMoreElements()) {

            final String httpHeaderName = httpHeaderNamesEnum.nextElement();

            final String[] values = this.request.getParameterValues(httpHeaderName);
            if (values != null) {
                for (final String value : values) {
                    list.add(new NameStringValuePair(httpHeaderName, value));
                }
            }

        }

        return list;

    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "http-request-headers",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_HTTP_HEADER,
            order = 40)
    public List<NameStringValuePair> getHttpRequestHeaders() {

        final List<NameStringValuePair> list = new ArrayList<NameStringValuePair>();

        final Enumeration<String> httpHeaderNamesEnum = this.request.getHeaderNames();
        while (httpHeaderNamesEnum.hasMoreElements()) {

            final String httpHeaderName = httpHeaderNamesEnum.nextElement();
            final String value = this.request.getHeader(httpHeaderName);
            list.add(new NameStringValuePair(httpHeaderName, value));

        }

        return list;
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "http-request-attributes",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_ATTRIBUTES,
            order = 45)
    public List<NameObjectValuePair> getHttpRequestAttributes() {

        final List<NameObjectValuePair> list = new ArrayList<NameObjectValuePair>();

        final Enumeration<String> attributeNames = this.request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {

            final String attributeName = attributeNames.nextElement();
            final Object value = this.request.getAttribute(attributeName);
            list.add(new NameObjectValuePair(attributeName, value));

        }

        return list;
    }


    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "cookies",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_COOKIES,
            order = 50)
    public List<ServletCookie> getCookies() {

        List<ServletCookie> wrappedCookies = new ArrayList<ServletCookie>();

        for (Cookie cookie : request.getCookies()) {
            wrappedCookies.add(new ServletCookie(cookie));
        }

        return wrappedCookies;
    }


    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "http-remote-address",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_REMOTE_ADDRESS,
            order = 150)
    public String getHttpRemoteAddress() {
        return request.getRemoteAddr();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "http-remote-host",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_REMOTE_HOST,
            order = 160)
    public String getHttpRemoteHost() {
        return this.request.getRemoteHost();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "http-remote-port",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_REMOTE_PORT,
            order = 170)
    public Integer getHttpRemotePort() {
        return this.request.getRemotePort();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "scheme",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_SCHEME,
            order = 200)
    public String getScheme() {
        return this.request.getScheme();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "is-secure",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_IS_SECURE,
            order = 210)
    public Boolean getSecure() {
        return this.request.isSecure();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "content-type",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_CONTENT_TYPE,
            order = 220)
    public String getContentType() {
        return this.request.getContentType();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "content-length",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_CONTENT_LENGTH,
            order = 230)
    public Integer getContentLength() {
        return this.request.getContentLength();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "locale",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_LOCALE,
            order = 240)
    public String getLocale() {
        return this.request.getLocale().toString();
    }
}
