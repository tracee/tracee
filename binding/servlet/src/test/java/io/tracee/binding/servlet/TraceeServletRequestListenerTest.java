package io.tracee.binding.servlet;

import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpHeaderTransport;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;


import static io.tracee.TraceeConstants.INVOCATION_ID_KEY;
import static io.tracee.TraceeConstants.SESSION_ID_KEY;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;

public class TraceeServletRequestListenerTest {

	private final HttpHeaderTransport transportSerialization = new HttpHeaderTransport();
	private final TraceeBackend backend = Mockito.mock(TraceeBackend.class);
	private final TraceeServletRequestListener unit = new TraceeServletRequestListener(backend, transportSerialization);
	private final HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
	private final TraceeFilterConfiguration configuration = Mockito.mock(TraceeFilterConfiguration.class);

	@Before
	public void setupMocks() {
		when(backend.getConfiguration()).thenReturn(configuration);
	}

	@Test
	public void testGeneratesInvocationId() throws Exception {
		when(configuration.shouldGenerateInvocationId()).thenReturn(true);
		when(httpServletRequest.getHeaders(TraceeConstants.TPIC_HEADER)).thenReturn(Collections.enumeration(Arrays.asList()));

		unit.requestInitialized(wrapToEvent(httpServletRequest));
		verify(backend, atLeastOnce()).put(eq(TraceeConstants.INVOCATION_ID_KEY), anyString());
	}

	@Test
	public void testDoesNotGeneratesInvocationId() throws Exception {
		when(configuration.shouldGenerateInvocationId()).thenReturn(false);
		when(httpServletRequest.getHeaders(TraceeConstants.TPIC_HEADER)).thenReturn(Collections.enumeration(Arrays.asList()));

		unit.requestInitialized(wrapToEvent(httpServletRequest));
		verify(backend, never()).put(eq(TraceeConstants.INVOCATION_ID_KEY), anyString());
	}


	@Test
	public void testAcceptIncomingInvocationId() throws Exception {
		when(configuration.shouldGenerateInvocationId()).thenReturn(false);
		when(configuration.shouldProcessContext(IncomingRequest)).thenReturn(true);
		when(configuration.filterDeniedParams(anyMapOf(String.class, String.class), any(TraceeFilterConfiguration.Channel.class))).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArguments()[0];
			}
		});
		when(httpServletRequest.getHeaders(TraceeConstants.TPIC_HEADER)).thenReturn(Collections.enumeration(
				Arrays.asList(INVOCATION_ID_KEY + "=123")));

		unit.requestInitialized(wrapToEvent(httpServletRequest));

		verify(backend, atLeastOnce()).putAll(Mockito.eq(new HashMap<String, String>() {{ put(INVOCATION_ID_KEY,"123"); }} ));
	}

	@Test
	public void testGenerateSessionId() throws Exception {
		when(configuration.shouldGenerateSessionId()).thenReturn(true);
		final HttpSession session = Mockito.mock(HttpSession.class);
		when(httpServletRequest.getSession(any(Boolean.class))).thenReturn(session);
		when(session.getId()).thenReturn("A_RANDOM_SESSION_ID");

		unit.requestInitialized(wrapToEvent(httpServletRequest));
		verify(backend, atLeastOnce()).put(eq(SESSION_ID_KEY), anyString());
	}

	@Test
	public void testClearsBackendAfterProcessing() {
		unit.requestDestroyed(new ServletRequestEvent(mock(ServletContext.class), httpServletRequest));
		verify(backend, atLeastOnce()).clear();
	}

	private ServletRequestEvent wrapToEvent(ServletRequest req) {
		return new ServletRequestEvent(mock(ServletContext.class), req);
	}
}
