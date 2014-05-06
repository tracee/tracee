package io.tracee.contextlogger.servlet;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TraceeErrorLoggingFilterTest {

	private final TraceeErrorLoggingFilter unit = new TraceeErrorLoggingFilter();
	private final HttpServletRequest request = mock(HttpServletRequest.class);
	private final HttpServletResponse response = mock(HttpServletResponse.class);
	private final FilterChain filterChain = mock(FilterChain.class);

	@Before
	public void setUpMocks() {

	}

	@Test
	public void isTransparentWhenEverythingIsFine() throws Exception {
		unit.doFilter(request,response,filterChain);
		verify(filterChain).doFilter(request,response);
	}

}
