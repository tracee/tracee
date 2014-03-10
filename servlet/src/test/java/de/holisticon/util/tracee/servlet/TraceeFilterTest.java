package de.holisticon.util.tracee.servlet;

import de.holisticon.util.tracee.SimpleTraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.TraceeLoggerFactory;
import de.holisticon.util.tracee.transport.HttpJsonHeaderTransport;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;


/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeFilterTest {

	private final SimpleTraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
	private final TraceeLoggerFactory traceeLoggerFactory = Mockito.mock(TraceeLoggerFactory.class);
	private final HttpJsonHeaderTransport transport =  new HttpJsonHeaderTransport(traceeLoggerFactory);
	private final TraceeFilter unit = new TraceeFilter(backend, transport);
	private final FilterChain filterChain = Mockito.mock(FilterChain.class);
	private final HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
	private final HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);



	@Test
	public void testWriteResponseHeaderEvenIfFilterChainThrowsAnException() throws Exception {

		backend.put("foobi", "yes sir");
		doThrow(new RuntimeException("Bad is bad")).when(filterChain).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));

		try {
			unit.doFilterHttp(httpServletRequest, httpServletResponse, filterChain);
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {}

		verify(httpServletResponse, atLeastOnce()).setHeader(eq(TraceeConstants.HTTP_HEADER_NAME),
				contains("\"foobi\":\"yes sir"));
	}

	@Test
	public void testDoesNotCreateAResponseHeaderWhenBackendIsEmpty() throws Exception {

		doThrow(new RuntimeException("Bad is bad")).when(filterChain).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));

		try {
			unit.doFilterHttp(httpServletRequest, httpServletResponse, filterChain);
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {}

		verify(httpServletResponse, never()).setHeader(anyString(), anyString());
	}

}
