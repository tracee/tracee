package de.holisticon.util.tracee.contextlogger.json.generator.datawrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Wrapper class for servlet data
 * 
 * @author Tobias Gindler, holisticon AG
 */
public final class ServletDataWrapper {

    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    private ServletDataWrapper(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) {
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
    }


    /**
     * Static method to create a ServletDataWrapper instance that wraps a a HttpServletRequest and HttpServletResponse instance.
     * @param httpServletRequest the request to wrap
     * @param httpServletResponse the response to wrap
     * @return the wrapper instance
     */
    public static ServletDataWrapper wrap(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) {
        return new ServletDataWrapper(httpServletRequest, httpServletResponse);
    }

    /**
     * Getter for the wrapped servlet request.
     * @return the wrapped request instance
     */
    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    /**
     * Getter for the wrapped servlet response.
     * @return the wrapped response instance
     */
    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

}
