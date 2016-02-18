package io.tracee.binding.springmvc;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.EmptyEnumeration;
import io.tracee.testhelper.FieldAccessUtil;
import io.tracee.testhelper.PermitAllTraceeFilterConfiguration;
import io.tracee.transport.HttpHeaderTransport;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static io.tracee.TraceeConstants.INVOCATION_ID_KEY;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
		when(httpServletRequest.getHeaders(TraceeConstants.TPIC_HEADER)).thenReturn(EmptyEnumeration.<String>emptyEnumeration());
	}

	@Test
	public void shouldSetInvocationIdToBackend() throws Exception {
		unit.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend).put(eq(INVOCATION_ID_KEY), anyString());
	}

	@Test
	public void shouldSetSessionIdToBackendHttpSessionExist() throws Exception {
		when(httpServletRequest.getSession(anyBoolean())).thenReturn(httpServletSession);
		when(httpServletSession.getId()).thenReturn(anyString());
		unit.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend).put(eq(TraceeConstants.SESSION_ID_KEY), anyString());
	}

	@Test
	public void shouldNotOverrideExistingInvocationId() throws Exception {
		when(mockedBackend.containsKey(eq(INVOCATION_ID_KEY))).thenReturn(true);
		unit.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend, never()).put(eq(INVOCATION_ID_KEY), anyString());
	}

	@Test
	public void shouldNotOverrideExistingSessionId() throws Exception {
		when(mockedBackend.containsKey(eq(TraceeConstants.SESSION_ID_KEY))).thenReturn(true);
		unit.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend, never()).put(eq(TraceeConstants.SESSION_ID_KEY), anyString());
	}

	@Test
	public void shouldWriteInitTpicToResponse() {
		final HashMap<String, String> parsedInitValues = new HashMap<>();
		parsedInitValues.put(INVOCATION_ID_KEY, "myGreatId321");
		when(mockedBackend.copyToMap()).thenReturn(parsedInitValues);

		when(httpServletResponse.isCommitted()).thenReturn(false);
		unit.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(httpServletResponse).setHeader(eq(TraceeConstants.TPIC_HEADER), contains(INVOCATION_ID_KEY + "=myGreatId321"));
	}

	@Test
	public void doNotWriteHeadersToResponseIfResponseIsCommited() {
		final HashMap<String, String> parsedInitValues = new HashMap<>();
		parsedInitValues.put(INVOCATION_ID_KEY, "myGreatId321");
		when(mockedBackend.copyToMap()).thenReturn(parsedInitValues);

		when(httpServletResponse.isCommitted()).thenReturn(true);
		unit.afterCompletion(httpServletRequest, httpServletResponse, new Object(), null);
		verify(httpServletResponse, times(0)).setHeader(eq(TraceeConstants.TPIC_HEADER), Mockito.anyString());
	}

	@Test
	public void initialTpicHeaderShouldBeReplacedAtTheEndIfResponseIsNotCommited() {
		final HashMap<String, String> parsedInitValues = new HashMap<>();
		parsedInitValues.put(INVOCATION_ID_KEY, "myGreatId321");
		when(mockedBackend.copyToMap()).thenReturn(parsedInitValues);
		when(httpServletResponse.isCommitted()).thenReturn(false);

		unit.preHandle(httpServletRequest, httpServletResponse, new Object());

		parsedInitValues.put(INVOCATION_ID_KEY, "myNewId45");
		unit.afterCompletion(httpServletRequest, httpServletResponse, new Object(), null);
		verify(httpServletResponse, times(1)).setHeader(eq(TraceeConstants.TPIC_HEADER), contains(INVOCATION_ID_KEY + "=myNewId45"));
		verify(httpServletResponse, times(2)).setHeader(eq(TraceeConstants.TPIC_HEADER), Mockito.anyString());
	}

	@Test
	public void shouldNotRenderContextInResponseIfConfigurationDeniesIt() throws Exception {
		final TraceeFilterConfiguration customFilterConfiguration = mock(TraceeFilterConfiguration.class);
		when(customFilterConfiguration.shouldProcessContext(TraceeFilterConfiguration.Channel.OutgoingResponse)).thenReturn(false);
		final TraceeBackend customBackend = mockedBackend(customFilterConfiguration);
		final TraceeInterceptor customUnit = new TraceeInterceptor(customBackend);
		mockedBackend.put(INVOCATION_ID_KEY, "123");
		customUnit.postHandle(httpServletRequest, httpServletResponse, new Object(), new ModelAndView());
		verify(httpServletResponse, never()).setHeader(eq(TraceeConstants.TPIC_HEADER), anyString());
	}

	@Test
	public void shouldRenderContextInResponseIfConfigured() throws Exception {
		mockedBackend.put(INVOCATION_ID_KEY, "123");
		unit.afterCompletion(httpServletRequest, httpServletResponse, new Object(), null);
		verify(httpServletResponse).setHeader(eq(TraceeConstants.TPIC_HEADER), anyString());
	}

	@Test
	public void shouldUseCustomHeaderInResponse() throws Exception {
		final String testHeader = "testHeader";
		unit.setOutgoingHeaderName(testHeader);
		unit.afterCompletion(httpServletRequest, httpServletResponse, new Object(), null);
		verify(httpServletResponse).setHeader(eq(testHeader), anyString());
	}

	@Test
	public void shouldUseConfiguredProfile() throws Exception {
		unit.setProfileName("FOO");
		unit.afterCompletion(httpServletRequest, httpServletResponse, new Object(), null);
		verify(mockedBackend).getConfiguration("FOO");
		assertThat(unit.getProfileName(), is("FOO"));
	}

	@Test
	public void shouldMergeIncomingContextIfConfigured() throws Exception {
		final Map<String, String> expected = new HashMap<>();
		expected.put("testkey", "testValue123");
		final Enumeration<String> header = createHeader("testkey", "testValue123");
		when(httpServletRequest.getHeaders(TraceeConstants.TPIC_HEADER)).thenReturn(header);
		unit.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend).putAll(eq(expected));
	}

	@Test
	public void shouldMergeIncomingContextFromCustomHeader() throws Exception {
		final String incomingHeader = "myHeader";
		unit.setIncomingHeaderName(incomingHeader);

		final Map<String, String> expected = new HashMap<>();
		expected.put("testkey", "testValue123");

		final Enumeration<String> headers = Collections.enumeration(singletonList("testkey=testValue123"));
		when(httpServletRequest.getHeaders(incomingHeader)).thenReturn(headers);
		unit.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend).putAll(eq(expected));
	}

	@Test
	public void shouldCleanupAfterProcessing() throws Exception {
		unit.afterCompletion(httpServletRequest, httpServletResponse, new Object(), null);
		verify(mockedBackend).clear();
	}

	@Test
	public void defaultConstructorUsesTraceeBackend() {
		final TraceeInterceptor interceptor = new TraceeInterceptor();
		MatcherAssert.assertThat((TraceeBackend) FieldAccessUtil.getFieldVal(interceptor, "backend"), is(Tracee.getBackend()));
	}

	private Enumeration<String> createHeader(String key, String value) {
		final Map<String, String> map = new HashMap<>();
		map.put(key, value);
		final String httpHeaderValue = new HttpHeaderTransport().render(map);
		return new Vector<>(Collections.singletonList(httpHeaderValue)).elements();
	}

	private TraceeBackend mockedBackend(TraceeFilterConfiguration filterConfiguration) {
		final TraceeBackend backend = mock(TraceeBackend.class);
		when(backend.getConfiguration()).thenReturn(filterConfiguration);
		when(backend.getConfiguration(anyString())).thenReturn(filterConfiguration);
		return backend;
	}
}
