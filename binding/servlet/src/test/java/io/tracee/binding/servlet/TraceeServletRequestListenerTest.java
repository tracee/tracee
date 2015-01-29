package io.tracee.binding.servlet;

import io.tracee.NoopTraceeLoggerFactory;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpHeaderTransport;
import io.tracee.transport.HttpRequestParameterTransport;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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
import java.util.Map;

import static io.tracee.TraceeConstants.REQUEST_ID_KEY;
import static io.tracee.TraceeConstants.SESSION_ID_KEY;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;
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

	private final HttpHeaderTransport transportSerialization = new HttpHeaderTransport(new NoopTraceeLoggerFactory());
	private final HttpRequestParameterTransport httpRequestParameterTransport = new HttpRequestParameterTransport(new NoopTraceeLoggerFactory());
	private final TraceeBackend backend = Mockito.mock(TraceeBackend.class);
	private final TraceeServletRequestListener unit = new TraceeServletRequestListener(backend, transportSerialization, httpRequestParameterTransport);
	private final HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
	private final TraceeFilterConfiguration configuration = Mockito.mock(TraceeFilterConfiguration.class);

	@Before
	public void setupMocks() {
		when(backend.getConfiguration()).thenReturn(configuration);
	}

	@Test
	public void testGeneratesRequestId() throws Exception {
		when(configuration.shouldGenerateRequestId()).thenReturn(true);
		when(httpServletRequest.getHeaders(TraceeConstants.HTTP_HEADER_NAME)).thenReturn(Collections.enumeration(Arrays.asList()));

		unit.requestInitialized(wrapToEvent(httpServletRequest));
		verify(backend, atLeastOnce()).put(eq(TraceeConstants.REQUEST_ID_KEY), anyString());
	}

	@Test
	public void testDoesNotGeneratesRequestId() throws Exception {
		when(configuration.shouldGenerateRequestId()).thenReturn(false);
		when(httpServletRequest.getHeaders(TraceeConstants.HTTP_HEADER_NAME)).thenReturn(Collections.enumeration(Arrays.asList()));

		unit.requestInitialized(wrapToEvent(httpServletRequest));
		verify(backend, never()).put(eq(TraceeConstants.REQUEST_ID_KEY), anyString());
	}


	@Test
	public void testAcceptIncomingRequestId() throws Exception {
		when(configuration.shouldGenerateRequestId()).thenReturn(false);
		when(configuration.shouldProcessContext(IncomingRequest)).thenReturn(true);
		when(configuration.filterDeniedParams(anyMapOf(String.class, String.class), any(TraceeFilterConfiguration.Channel.class))).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArguments()[0];
			}
		});
		when(httpServletRequest.getHeaders(TraceeConstants.HTTP_HEADER_NAME)).thenReturn(Collections.enumeration(
				Arrays.asList(REQUEST_ID_KEY + "=123")));

		unit.requestInitialized(wrapToEvent(httpServletRequest));

		verify(backend, atLeastOnce()).putAll(Mockito.eq(new HashMap<String, String>() {{ put(REQUEST_ID_KEY,"123"); }} ));
	}

	@Test
	public void testAcceptIncomingRequestAndSessionIdFromParameter() throws Exception {
		when(configuration.shouldGenerateRequestId()).thenReturn(false);
		when(configuration.shouldProcessContext(IncomingRequest)).thenReturn(true);
		when(configuration.filterDeniedParams(anyMapOf(String.class, String.class), any(TraceeFilterConfiguration.Channel.class))).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArguments()[0];
			}
		});
		when(httpServletRequest.getParameterMap()).thenReturn(
				new HashMap<String, String[]>() {{
					put(REQUEST_ID_KEY, new String[]{"124"});
					put(SESSION_ID_KEY, new String[]{"987"});
				}});

		unit.requestInitialized(wrapToEvent(httpServletRequest));

		verify(backend, atLeastOnce()).putAll(Mockito.eq(new HashMap<String, String>() {{
			put(REQUEST_ID_KEY, "124");
			put(SESSION_ID_KEY, "987");
		}}));
	}

	@Test
	public void testHeaderRequestAndSessionIdOverwriteRequestAndSessionIdFromParameter() throws Exception {
		when(configuration.shouldGenerateRequestId()).thenReturn(false);
		when(configuration.shouldProcessContext(IncomingRequest)).thenReturn(true);
		when(configuration.filterDeniedParams(anyMapOf(String.class, String.class), any(TraceeFilterConfiguration.Channel.class))).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArguments()[0];
			}
		});
		when(httpServletRequest.getParameterMap()).thenReturn(
				new HashMap<String, String[]>() {{
					put(REQUEST_ID_KEY, new String[]{"124"});
					put(SESSION_ID_KEY, new String[]{"16061981"});
				}});
		when(httpServletRequest.getHeaders(TraceeConstants.HTTP_HEADER_NAME)).thenReturn(Collections.enumeration(
				Arrays.asList(REQUEST_ID_KEY + "=123", SESSION_ID_KEY + "=08081988")));

		ArgumentCaptor<Map> argument = ArgumentCaptor.forClass(Map.class);

		unit.requestInitialized(wrapToEvent(httpServletRequest));

		verify(backend, atLeastOnce()).putAll(argument.capture());
		assertThat(argument.getAllValues(), hasItem(new HashMap<String, String>() {{
			put(REQUEST_ID_KEY, "123");
			put(SESSION_ID_KEY, "08081988");
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

	private ServletRequestEvent wrapToEvent(ServletRequest req) {
		return new ServletRequestEvent(mock(ServletContext.class), req);
	}
}
