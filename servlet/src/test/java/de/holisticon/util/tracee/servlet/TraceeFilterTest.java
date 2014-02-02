package de.holisticon.util.tracee.servlet;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.Utilities;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeFilterTest {

    private final TraceeBackend backend = Tracee.getBackend();
    private final TraceeFilter unit = new TraceeFilter();
    private final FilterChain filterChain = Mockito.mock(FilterChain.class);
    private final HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
    private final HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
    private final FilterConfig filterConfig = Mockito.mock(FilterConfig.class);

    @Before
    public void setUp() throws ServletException {
        when(filterConfig.getInitParameter(TraceeFilter.ACCEPT_INCOMING_CONTEXT_KEY)).thenReturn("true");
        when(filterConfig.getInitParameter(TraceeFilter.RESPOND_WITH_CONTEXT_KEY)).thenReturn("true");
        when(httpServletRequest.getHeaders(TraceeConstants.HTTP_HEADER_NAME)).thenReturn(Utilities.toEnumeration(Arrays.asList()));
                unit.init(filterConfig);
    }

    @Test
    public void testDoFilterRespondsWithRequestId() throws Exception {
        unit.doFilter(httpServletRequest,httpServletResponse, filterChain);
        verify(httpServletResponse).setHeader(eq(TraceeConstants.HTTP_HEADER_NAME),
                contains("\"" + TraceeConstants.REQUEST_ID_KEY + "\":\""));
    }




}
