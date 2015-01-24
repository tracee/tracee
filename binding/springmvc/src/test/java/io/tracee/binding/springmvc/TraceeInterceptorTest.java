package io.tracee.binding.springmvc;

import io.tracee.*;
import io.tracee.configuration.TraceeFilterConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static io.tracee.TraceeConstants.REQUEST_ID_KEY;
import static io.tracee.TraceeConstants.SESSION_ID_KEY;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;

public class TraceeInterceptorTest {

	private final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
	private final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
	private final HttpSession httpServletSession = mock(HttpSession.class);
	private TraceeInterceptor unit;
	private TraceeBackend mockedBackend;

	@Before
	public void beforeTest() {
		mockedBackend = mockedBackend(new PermitAllTraceeFilterConfiguration());
		unit = new TraceeInterceptor(mockedBackend);
		when(httpServletRequest.getHeaders(TraceeConstants.HTTP_HEADER_NAME)).thenReturn(EmptyEnumeration.emptyEnumeration());
	}

	@Test
	public void shouldSetRequestIdToBackend() throws Exception {
		unit.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend).put(eq(TraceeConstants.REQUEST_ID_KEY), anyString());
	}

	@Test
	public void shouldSetSessionIdToBackendHttpSessionExist() throws Exception {
		when(httpServletRequest.getSession(anyBoolean())).thenReturn(httpServletSession);
		when(httpServletSession.getId()).thenReturn(anyString());
		unit.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend).put(eq(TraceeConstants.SESSION_ID_KEY), anyString());
	}

	@Test
	public void shouldNotOverrideExistingRequestId() throws Exception {
		when(mockedBackend.containsKey(eq(TraceeConstants.REQUEST_ID_KEY))).thenReturn(true);
		unit.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend, never()).put(eq(TraceeConstants.REQUEST_ID_KEY), anyString());
	}

	@Test
	public void shouldNotOverrideExistingSessionId() throws Exception {
		when(mockedBackend.containsKey(eq(TraceeConstants.SESSION_ID_KEY))).thenReturn(true);
		unit.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend, never()).put(eq(TraceeConstants.SESSION_ID_KEY), anyString());
	}

	@Test
	public void shouldNotRenderContextInResponseIfConfigurationDeniesIt() throws Exception {
		final TraceeFilterConfiguration customFilterConfiguration = mock(TraceeFilterConfiguration.class);
		when(customFilterConfiguration.shouldProcessContext(TraceeFilterConfiguration.Channel.OutgoingResponse)).thenReturn(false);
		final TraceeBackend customBackend = mockedBackend(customFilterConfiguration);
		final TraceeInterceptor customUnit = new TraceeInterceptor(customBackend);
		mockedBackend.put(TraceeConstants.REQUEST_ID_KEY, "123");
		customUnit.postHandle(httpServletRequest, httpServletResponse, new Object(), new ModelAndView());
		verify(httpServletResponse, never()).setHeader(eq(TraceeConstants.HTTP_HEADER_NAME), anyString());
	}

	@Test
	public void shouldRenderContextInResponseIfConfigured() throws Exception {
		mockedBackend.put(TraceeConstants.REQUEST_ID_KEY, "123");
		unit.postHandle(httpServletRequest, httpServletResponse, new Object(), new ModelAndView());
		verify(httpServletResponse).setHeader(eq(TraceeConstants.HTTP_HEADER_NAME), anyString());
	}

	@Test
	public void testAcceptIncomingRequestAndSessionIdFromParameter() throws Exception {
		when(mockedBackend.containsKey(eq(TraceeConstants.REQUEST_ID_KEY))).thenReturn(false);
		when(httpServletRequest.getParameterMap()).thenReturn(
				new HashMap<String, String[]>() {{
					put(REQUEST_ID_KEY, new String[]{"124"});
					put(SESSION_ID_KEY, new String[]{"987"});
				}});

		unit.preHandle(httpServletRequest, httpServletResponse, new Object());

		verify(mockedBackend, atLeastOnce()).putAll(Mockito.eq(new HashMap<String, String>() {{
			put(REQUEST_ID_KEY, "124");
			put(SESSION_ID_KEY, "987");
		}}));
	}

	@Test
	public void testHeaderRequestAndSessionIdOverwriteRequestAndSessionIdFromParameter() throws Exception {
		when(mockedBackend.containsKey(eq(TraceeConstants.REQUEST_ID_KEY))).thenReturn(false);
		when(httpServletRequest.getParameterMap()).thenReturn(
				new HashMap<String, String[]>() {{
					put(REQUEST_ID_KEY, new String[]{"124"});
					put(SESSION_ID_KEY, new String[]{"16061981"});
				}});
		when(httpServletRequest.getHeaders(TraceeConstants.HTTP_HEADER_NAME)).thenReturn(Collections.enumeration(
				Arrays.asList(REQUEST_ID_KEY + "=123", SESSION_ID_KEY + "=08081988")));

		ArgumentCaptor<Map> argument = ArgumentCaptor.forClass(Map.class);

		unit.preHandle(httpServletRequest, httpServletResponse, new Object());

		verify(mockedBackend, atLeastOnce()).putAll(argument.capture());
		assertThat(argument.getAllValues(), hasItem(new HashMap<String, String>() {{
			put(REQUEST_ID_KEY, "123");
			put(SESSION_ID_KEY, "08081988");
		}}));
	}

	@Test
	public void shouldUseCustomHeaderInResponse() throws Exception {
		final String testHeader = "testHeader";
		unit.setOutgoingHeaderName(testHeader);
		unit.postHandle(httpServletRequest, httpServletResponse, new Object(), new ModelAndView());
		verify(httpServletResponse).setHeader(eq(testHeader), anyString());
	}

	@Test
	public void shouldUseConfiguredProfile() throws Exception {
		unit.setProfileName("FOO");
		unit.postHandle(httpServletRequest, httpServletResponse, new Object(), new ModelAndView());
		verify(mockedBackend).getConfiguration("FOO");
	}

	@Test
	public void shouldMergeIncomingContextIfConfigured() throws Exception {
		final Map<String, String> expected = new HashMap<String, String>();
		expected.put("testkey", "testValue123");
		final Vector<String> headers = new Vector<String>();
		headers.add("testkey=testValue123");
		when(httpServletRequest.getHeaders(TraceeConstants.HTTP_HEADER_NAME)).thenReturn(headers.elements());
		unit.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend).putAll(eq(expected));
	}

	@Test
	public void shouldMergeIncomingContextFromCustomHeader() throws Exception {
		final String incomingHeader = "myHeader";
		unit.setIncomingHeaderName(incomingHeader);

		final Map<String, String> expected = new HashMap<String, String>();
		expected.put("testkey", "testValue123");

		final Enumeration<String> headers = Collections.enumeration(Arrays.asList("testkey=testValue123"));
		when(httpServletRequest.getHeaders(incomingHeader)).thenReturn(headers);
		unit.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend).putAll(eq(expected));
	}

	@Test
	public void shouldCleanupAfterProcessing() throws Exception {
		unit.postHandle(httpServletRequest, httpServletResponse, new Object(), null);
		verify(mockedBackend).clear();
	}

	private TraceeBackend mockedBackend(TraceeFilterConfiguration filterConfiguration) {
		final TraceeBackend backend = mock(TraceeBackend.class);
		final NoopTraceeLoggerFactory noopTraceeLoggerFactory = new NoopTraceeLoggerFactory();
		when(backend.getLoggerFactory()).thenReturn(noopTraceeLoggerFactory);
		when(backend.getConfiguration()).thenReturn(filterConfiguration);
		when(backend.getConfiguration(anyString())).thenReturn(filterConfiguration);
		return backend;
	}
}
