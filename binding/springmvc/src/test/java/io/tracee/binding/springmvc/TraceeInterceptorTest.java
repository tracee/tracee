package io.tracee.binding.springmvc;

import io.tracee.*;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.EmptyEnumeration;
import io.tracee.testhelper.FieldAccessUtil;
import io.tracee.testhelper.PermitAllTraceeFilterConfiguration;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
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
		when(httpServletRequest.getHeaders(TraceeConstants.TPIC_HEADER)).thenReturn(EmptyEnumeration.emptyEnumeration());
	}

	@Test
	public void shouldSetInvocationIdToBackend() throws Exception {
		unit.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend).put(eq(TraceeConstants.INVOCATION_ID_KEY), anyString());
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
		when(mockedBackend.containsKey(eq(TraceeConstants.INVOCATION_ID_KEY))).thenReturn(true);
		unit.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend, never()).put(eq(TraceeConstants.INVOCATION_ID_KEY), anyString());
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
		mockedBackend.put(TraceeConstants.INVOCATION_ID_KEY, "123");
		customUnit.postHandle(httpServletRequest, httpServletResponse, new Object(), new ModelAndView());
		verify(httpServletResponse, never()).setHeader(eq(TraceeConstants.TPIC_HEADER), anyString());
	}

	@Test
	public void shouldRenderContextInResponseIfConfigured() throws Exception {
		mockedBackend.put(TraceeConstants.INVOCATION_ID_KEY, "123");
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
		final Map<String, String> expected = new HashMap<String, String>();
		expected.put("testkey", "testValue123");
		final Vector<String> headers = new Vector<String>();
		headers.add("testkey=testValue123");
		when(httpServletRequest.getHeaders(TraceeConstants.TPIC_HEADER)).thenReturn(headers.elements());
		unit.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend).putAll(eq(expected));
	}

	@Test
	public void shouldMergeIncomingContextFromCustomHeader() throws Exception {
		final String incomingHeader = "myHeader";
		unit.setIncomingHeaderName(incomingHeader);

		final Map<String, String> expected = new HashMap<String, String>();
		expected.put("testkey", "testValue123");

		final Enumeration<String> headers = Collections.enumeration(Collections.singletonList("testkey=testValue123"));
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

	private TraceeBackend mockedBackend(TraceeFilterConfiguration filterConfiguration) {
		final TraceeBackend backend = mock(TraceeBackend.class);
		when(backend.getConfiguration()).thenReturn(filterConfiguration);
		when(backend.getConfiguration(anyString())).thenReturn(filterConfiguration);
		return backend;
	}
}
