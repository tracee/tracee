package de.holisticon.util.tracee.contextlogger.json.generator;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.holisticon.util.tracee.contextlogger.json.beans.ServletCategory;
import de.holisticon.util.tracee.contextlogger.json.beans.servlet.ServletCookie;
import de.holisticon.util.tracee.contextlogger.json.beans.servlet.ServletRemoteInfoSubCategory;
import de.holisticon.util.tracee.contextlogger.json.beans.servlet.ServletRequestEnhancedInfoSubCategory;
import de.holisticon.util.tracee.contextlogger.json.beans.servlet.ServletRequestSubCategory;
import de.holisticon.util.tracee.contextlogger.json.beans.servlet.ServletResponseSubCategory;
import de.holisticon.util.tracee.contextlogger.json.beans.servlet.ServletSessionSubCategory;
import de.holisticon.util.tracee.contextlogger.json.beans.values.ServletHttpHeader;
import de.holisticon.util.tracee.contextlogger.json.beans.values.ServletHttpParameter;
import de.holisticon.util.tracee.contextlogger.json.beans.values.ServletRequestAttribute;
import de.holisticon.util.tracee.contextlogger.json.beans.values.ServletSessionAttribute;
import de.holisticon.util.tracee.contextlogger.json.generator.datawrapper.ServletDataWrapper;
import de.holisticon.util.tracee.contextlogger.presets.Preset;

/**
 * Factory for servlet context specific data.
 * <p/>
 * Created by Tobias Gindler, holisticon AG on 20.12.13.
 */
public final class ServletCategoryCreator {

    private ServletCategoryCreator() {
        // hide constructor
    }

    public static ServletCategory createServletCategory(final ServletDataWrapper servletDataWrapper) {

        final HttpServletRequest httpServletRequest = servletDataWrapper.getHttpServletRequest();
        final HttpServletResponse httpServletResponse = servletDataWrapper.getHttpServletResponse();

        final ServletRequestSubCategory servletRequestSubCategory = createRequestSubcategory(httpServletRequest);
        final ServletResponseSubCategory servletResponseSubCategory = createServletResponseSubCategory(httpServletResponse);
        final ServletSessionSubCategory servletSessionSubCategory = createServletSessionSubCategory(httpServletRequest);
        final List<ServletRequestAttribute> servletRequestAttributes = createServletRequestAttributes(httpServletRequest);

        return new ServletCategory(Preset.getPreset().getPresetConfig().showServletRequest() ? servletRequestSubCategory : null, Preset.getPreset()
                .getPresetConfig().showServletResponse() ? servletResponseSubCategory : null, Preset.getPreset().getPresetConfig().showServletSession()
                ? servletSessionSubCategory : null, Preset.getPreset().getPresetConfig().showServletRequestAttributes() ? servletRequestAttributes
                : null);


    }

    private static ServletRequestSubCategory createRequestSubcategory(final HttpServletRequest request) {

        final String url = request.getRequestURL().toString();
        final String httpMethod = request.getMethod();
        final List<ServletHttpParameter> httpParameters = getHttpParameters(request);
        final List<ServletHttpHeader> httpHeaders = getRequestHttpHeaders(request);

        final ServletRemoteInfoSubCategory remoteInfo = createRequestRemoteInfo(request);
        final ServletRequestEnhancedInfoSubCategory enhancedRequestInfo = createServletRequestEnhancedInfoSubCategory(request);
        final List<ServletCookie> cookies = createServletCookies(request);

        return new ServletRequestSubCategory(url, httpMethod, httpParameters, Preset.getPreset().getPresetConfig().showServletRequestHttpHeaders()
                ? httpHeaders : null, Preset.getPreset().getPresetConfig().showServletRequestRemoteInfo() ? remoteInfo : null, Preset.getPreset()
                .getPresetConfig().showServletRequestEnhancedInfo() ? enhancedRequestInfo : null, Preset.getPreset().getPresetConfig()
                .showServletRequestCookies() ? cookies : null);
    }

    private static ServletRemoteInfoSubCategory createRequestRemoteInfo(final HttpServletRequest request) {

        final String httpRemoteAddress = request.getRemoteAddr();
        final String httpRemoteHost = request.getRemoteHost();
        final Integer httpRemotePort = request.getRemotePort();

        return new ServletRemoteInfoSubCategory(httpRemoteAddress, httpRemoteHost, httpRemotePort);
    }

    private static ServletRequestEnhancedInfoSubCategory createServletRequestEnhancedInfoSubCategory(final HttpServletRequest request) {

        final String scheme = request.getScheme();
        final Boolean secure = request.isSecure();
        final String contentType = request.getContentType();
        final Integer contentLength = request.getContentLength();
        final String locale = request.getLocale().toString();

        return new ServletRequestEnhancedInfoSubCategory(scheme, secure, contentType, contentLength, locale);
    }

    private static List<ServletCookie> createServletCookies(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            final List<ServletCookie> transformedCookies = new ArrayList<ServletCookie>();

            for (final Cookie cookie : cookies) {
                transformedCookies.add(new ServletCookie(cookie));
            }

            return transformedCookies;

        } else {
            return null;
        }

    }

    private static ServletResponseSubCategory createServletResponseSubCategory(final HttpServletResponse response) {

        try {

            final Integer statusCode = response.getStatus();
            final List<ServletHttpHeader> httpHeaders = getResponseHttpHeaders(response);

            return new ServletResponseSubCategory(statusCode, Preset.getPreset().getPresetConfig().showServletResponseHttpHeaders() ? httpHeaders : null);
        } catch (final Exception e) {

            // handle servlet apis &lt; 3.0
            return null;

        }
    }

    private static ServletSessionSubCategory createServletSessionSubCategory(final HttpServletRequest request) {

        final Boolean sessionExists = (request.getSession(false) != null);
        String userName = null;
        List<ServletSessionAttribute> sessionAttributes = null;

        if (sessionExists) {
            userName = getUser(request);
            sessionAttributes = createServletSessionAttributes(request);
        }

        return new ServletSessionSubCategory(sessionExists, userName, Preset.getPreset().getPresetConfig().showServletSessionAttributes()
                ? sessionAttributes : null);
    }

    private static List<ServletHttpHeader> getRequestHttpHeaders(final HttpServletRequest httpServletRequest) {

        final List<ServletHttpHeader> list = new ArrayList<ServletHttpHeader>();

        final Enumeration<String> httpHeaderNamesEnum = httpServletRequest.getHeaderNames();
        while (httpHeaderNamesEnum.hasMoreElements()) {

            final String httpHeaderName = httpHeaderNamesEnum.nextElement();
            final String value = httpServletRequest.getHeader(httpHeaderName);
            list.add(new ServletHttpHeader(httpHeaderName, value));

        }

        return list;

    }

    private static List<ServletHttpHeader> getResponseHttpHeaders(final HttpServletResponse httpServletResponse) {

        final List<ServletHttpHeader> list = new ArrayList<ServletHttpHeader>();

        final Collection<String> httpHeaderNames = httpServletResponse.getHeaderNames();
        for (final String httpHeaderName : httpHeaderNames) {

            final String value = httpServletResponse.getHeader(httpHeaderName);
            list.add(new ServletHttpHeader(httpHeaderName, value));

        }

        return list;

    }

    private static List<ServletHttpParameter> getHttpParameters(final HttpServletRequest httpServletRequest) {

        final List<ServletHttpParameter> list = new ArrayList<ServletHttpParameter>();

        final Enumeration<String> httpHeaderNamesEnum = httpServletRequest.getParameterNames();
        while (httpHeaderNamesEnum.hasMoreElements()) {

            final String httpHeaderName = httpHeaderNamesEnum.nextElement();

            final String[] values = httpServletRequest.getParameterValues(httpHeaderName);
            if (values != null) {
                for (final String value : values) {
                    list.add(new ServletHttpParameter(httpHeaderName, value));
                }
            }

        }

        return list;

    }

    private static List<ServletSessionAttribute> createServletSessionAttributes(final HttpServletRequest request) {

        final HttpSession session = request.getSession(false);

        final List<ServletSessionAttribute> sessionAttributes = new ArrayList<ServletSessionAttribute>();

        if (session != null) {
            final Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {

                final String key = attributeNames.nextElement();
                final Object value = session.getAttribute(key);

                sessionAttributes.add(new ServletSessionAttribute(key, value != null ? value.toString() : null));

            }
        }

        return (sessionAttributes.size() > 0 ? sessionAttributes : null);
    }

    private static List<ServletRequestAttribute> createServletRequestAttributes(final HttpServletRequest request) {

        final List<ServletRequestAttribute> requestAttributes = new ArrayList<ServletRequestAttribute>();

        final Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {

            final String key = attributeNames.nextElement();
            final Object value = request.getAttribute(key);

            requestAttributes.add(new ServletRequestAttribute(key, value != null ? value.toString() : null));

        }

        return (requestAttributes.size() > 0 ? requestAttributes : null);
    }

    private static String getUser(final HttpServletRequest httpServletRequest) {
        final Principal userPrincipal = httpServletRequest.getUserPrincipal();
        final String user;
        if (userPrincipal != null) {
            user = userPrincipal.getName();
        } else {
            user = null;
        }
        return user;
    }

}
