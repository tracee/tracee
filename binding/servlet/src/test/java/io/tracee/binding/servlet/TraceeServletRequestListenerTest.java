package io.tracee.binding.servlet;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.FieldAccessUtil;
import io.tracee.transport.HttpHeaderTransport;
import org.hamcrest.MatcherAssert;
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import static io.tracee.TraceeConstants.INVOCATION_ID_KEY;
import static io.tracee.TraceeConstants.SESSION_ID_KEY;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;
import static java.util.Collections.enumeration;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
		final Collection<String> emptyList = Collections.emptyList();
		when(httpServletRequest.getHeaders(TraceeConstants.TPIC_HEADER)).thenReturn(enumeration(emptyList));

		unit.requestInitialized(wrapToEvent(httpServletRequest));
		verify(backend, atLeastOnce()).put(eq(TraceeConstants.INVOCATION_ID_KEY), anyString());
	}

	@Test
	public void testDoesNotGeneratesInvocationId() throws Exception {
		when(configuration.shouldGenerateInvocationId()).thenReturn(false);
		final Collection<String> emptyList = Collections.emptyList();
		when(httpServletRequest.getHeaders(TraceeConstants.TPIC_HEADER)).thenReturn(enumeration(emptyList));

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
		when(httpServletRequest.getHeaders(TraceeConstants.TPIC_HEADER)).thenReturn(enumeration(
			singletonList(INVOCATION_ID_KEY + "=123")));

		unit.requestInitialized(wrapToEvent(httpServletRequest));

		verify(backend, atLeastOnce()).putAll(Mockito.eq(new HashMap<String, String>() {{
			put(INVOCATION_ID_KEY, "123");
		}}));
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

	@Test
	public void defaultConstructorUsesTraceeBackend() {
		final TraceeSessionListener listener = new TraceeSessionListener();
		MatcherAssert.assertThat((TraceeBackend) FieldAccessUtil.getFieldVal(listener, "backend"), is(Tracee.getBackend()));
	}

	private ServletRequestEvent wrapToEvent(ServletRequest req) {
		return new ServletRequestEvent(mock(ServletContext.class), req);
	}
}
