package de.holisticon.util.tracee.errorlogger.json.generator;

import de.holisticon.util.tracee.errorlogger.json.beans.ServletCategory;
import de.holisticon.util.tracee.errorlogger.json.beans.values.ServletHttpHeader;
import de.holisticon.util.tracee.errorlogger.json.beans.values.ServletHttpParameter;

import java.security.Principal;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Tobias Gindler, holisticon AG on 20.12.13.
 */
public final class ServletCategoryCreator {

    private ServletCategoryCreator() {
        // hide constructor
    }

    public static ServletCategory createServletCategory(ServletRequest request, ServletResponse response) {

        String url = null;
        String httpMethod = null;
        List<ServletHttpParameter> httpParameters = null;
        List<ServletHttpHeader> httpHeader = null;
        String user = null;
        String remoteAddress = null;
        String remoteHost = null;
        Integer remotePort = null;

        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {

            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            url = httpServletRequest.getRequestURL().toString();
            httpMethod = httpServletRequest.getMethod();
            user = getUser(httpServletRequest);

            httpParameters = getHttpParameters(httpServletRequest);
            httpHeader = getHttpHeaders(httpServletRequest);

            remoteAddress = httpServletRequest.getRemoteAddr();
            remoteHost = httpServletRequest.getRemoteHost();
            remotePort = httpServletRequest.getRemotePort();

        }

        return new ServletCategory(
                url,
                httpMethod,
                httpParameters,
                httpHeader,
                remoteAddress,
                remoteHost,
                remotePort,
                user);

    }

    private static List<ServletHttpHeader> getHttpHeaders(HttpServletRequest httpServletRequest) {

        List<ServletHttpHeader> list = new ArrayList<ServletHttpHeader>();

        Enumeration<String> httpHeaderNamesEnum = httpServletRequest.getHeaderNames();
        while (httpHeaderNamesEnum.hasMoreElements()) {

            String httpHeaderName = httpHeaderNamesEnum.nextElement();
            String value = httpServletRequest.getHeader(httpHeaderName);
            list.add(new ServletHttpHeader(httpHeaderName, value));

        }


        return list;

    }

    private static List<ServletHttpParameter> getHttpParameters(HttpServletRequest httpServletRequest) {

        List<ServletHttpParameter> list = new ArrayList<ServletHttpParameter>();

        Enumeration<String> httpHeaderNamesEnum = httpServletRequest.getParameterNames();
        while (httpHeaderNamesEnum.hasMoreElements()) {

            String httpHeaderName = httpHeaderNamesEnum.nextElement();

            String[] values = httpServletRequest.getParameterValues(httpHeaderName);
            if (values != null) {
                for (String value : values) {
                    list.add(new ServletHttpParameter(httpHeaderName, value));
                }
            }

        }

        return list;

    }

    private static String getUser(HttpServletRequest httpServletRequest) {
        Principal userPrincipal = httpServletRequest.getUserPrincipal();
        final String user;
        if (userPrincipal != null) {
            user = userPrincipal.getName();
        } else {
            user = null;
        }
        return user;
    }


}
