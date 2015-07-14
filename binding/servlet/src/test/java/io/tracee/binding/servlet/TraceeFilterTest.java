package io.tracee.binding.servlet;

import io.tracee.Tracee;
import io.tracee.testhelper.FieldAccessUtil;
import io.tracee.testhelper.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.transport.HttpHeaderTransport;

import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;

public class TraceeFilterTest {

	private final SimpleTraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
	private final HttpHeaderTransport transport =  new HttpHeaderTransport();
	private final TraceeFilter unit = new TraceeFilter(backend, transport);
	private final FilterChain filterChain = Mockito.mock(FilterChain.class);
	private final HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
	private final HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);


	@Test
	public void testUsesProfileIfConfigured() throws ServletException, IOException {
		final FilterConfig filterConfigWithProfile = mock(FilterConfig.class);
		when(filterConfigWithProfile.getInitParameter(TraceeFilter.PROFILE_INIT_PARAM)).thenReturn("FOO");
		final TraceeBackend spiedBackend = spy(backend);
		final TraceeFilter unitWithSpiedBackend = new TraceeFilter(spiedBackend, transport);
		unitWithSpiedBackend.init(filterConfigWithProfile);
		unitWithSpiedBackend.doFilterHttp(httpServletRequest, httpServletResponse, filterChain);
		verify(spiedBackend).getConfiguration(eq("FOO"));
	}

	@Test
	public void testWriteResponseHeaderEvenIfFilterChainThrowsAnException() throws Exception {

		backend.put("foobi", "yes sir");
		doThrow(new RuntimeException("Bad is bad")).when(filterChain).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));

		try {
			unit.doFilter(httpServletRequest, httpServletResponse, filterChain);
			fail("Expected RuntimeException");
		} catch (RuntimeException ignored) { /*ignore*/ }

		verify(httpServletResponse, atLeastOnce()).setHeader(eq(TraceeConstants.TPIC_HEADER),
				contains("foobi=yes+sir"));
	}

	@Test
	public void testDoesNotCreateAResponseHeaderWhenBackendIsEmpty() throws Exception {

		doThrow(new RuntimeException("Bad is bad")).when(filterChain).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));

		try {
			unit.doFilter(httpServletRequest, httpServletResponse, filterChain);
			fail("Expected RuntimeException");
		} catch (RuntimeException ignored) { /*ignore*/ }

		verify(httpServletResponse, never()).setHeader(anyString(), anyString());
	}

	@Test
	public void shouldDelegateToNextInFilterChainForNonHttpRequests() throws Exception {
		ServletRequest servletRequest = mock(ServletRequest.class);
		ServletResponse servletResponse = mock(ServletResponse.class);
		unit.doFilter(servletRequest, servletResponse, filterChain);
		verify(filterChain).doFilter(servletRequest, servletResponse);
	}

	@Test
	public void shouldDelegateToNextInFilterChainForHttpRequests() throws Exception {
		unit.doFilter(httpServletRequest, httpServletResponse, filterChain);
		verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
	}

	@Test
	public void defaultConstructorUsesTraceeBackend() {
		final TraceeFilter filter = new TraceeFilter();
		MatcherAssert.assertThat((TraceeBackend) FieldAccessUtil.getFieldVal(filter, "backend"), is(Tracee.getBackend()));
	}
}
