package de.holisticon.util.tracee.springmvc;

import com.google.gson.Gson;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.Utilities;
import de.holisticon.util.tracee.springmvc.testutils.TestLogBackend;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Sven Bunge, Holisticon AG
 */
public class TraceeInterceptorTest {

	private final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
	private final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
	private final HttpSession httpServletSession = mock(HttpSession.class);
	private TraceeInterceptor outInterceptor;
	private TraceeBackend mockedBackend;

	@Before
	public void beforeTest() {
		mockedBackend = mockedBackend();
		outInterceptor = new TraceeInterceptor(mockedBackend);
	}

	@Test
	public void shouldSetRequestIdToBackend() throws Exception {
		outInterceptor.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend).put(eq(TraceeConstants.REQUEST_ID_KEY), anyString());
	}

	@Test
	public void shouldSetSessionIdToBackendHttpSessionExist() throws Exception {
		when(httpServletRequest.getSession(anyBoolean())).thenReturn(httpServletSession);
		when(httpServletSession.getId()).thenReturn(anyString());
		outInterceptor.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend).put(eq(TraceeConstants.SESSION_ID_KEY), anyString());
	}

	@Test
	public void shouldNotOverrideExistingRequestId() throws Exception {
		when(mockedBackend.containsKey(eq(TraceeConstants.REQUEST_ID_KEY))).thenReturn(true);
		outInterceptor.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend, never()).put(eq(TraceeConstants.REQUEST_ID_KEY), anyString());
	}

	@Test
	public void shouldNotOverrideExistingSessionId() throws Exception {
		when(mockedBackend.containsKey(eq(TraceeConstants.SESSION_ID_KEY))).thenReturn(true);
		outInterceptor.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend, never()).put(eq(TraceeConstants.SESSION_ID_KEY), anyString());
	}

	@Test
	public void shouldNotRenderContextInResponseIfNotConigured() throws Exception {
		mockedBackend.put(TraceeConstants.REQUEST_ID_KEY, "123");
		outInterceptor.postHandle(httpServletRequest, httpServletResponse, new Object(), new ModelAndView());
		verify(httpServletResponse, never()).setHeader(eq(TraceeConstants.HTTP_HEADER_NAME), anyString());
	}

	@Test
	public void shouldRenderContextInResponseIfConfigured() throws Exception {
		outInterceptor.setRespondWithContext(true);
		mockedBackend.put(TraceeConstants.REQUEST_ID_KEY, "123");
		outInterceptor.postHandle(httpServletRequest, httpServletResponse, new Object(), new ModelAndView());
		verify(httpServletResponse).setHeader(eq(TraceeConstants.HTTP_HEADER_NAME), anyString());
	}

	@Test
	public void shouldUseCustomHeaderInResponse() throws Exception {
		outInterceptor.setRespondWithContext(true);
		final String testHeader = "testHeader";
		outInterceptor.setOutgoingHeaderName(testHeader);
		outInterceptor.postHandle(httpServletRequest, httpServletResponse, new Object(), new ModelAndView());
		verify(httpServletResponse).setHeader(eq(testHeader), anyString());
	}

	@Test
	public void shouldMergeIncomingContextIfConfigured() throws Exception {
		outInterceptor.setAcceptIncomingContext(true);
		final Map<String, String> expected = new HashMap<String, String>();
		expected.put("testkey", "testValue123");
		final Vector<String> headers = new Vector<String>();
		headers.add(new Gson().toJson(expected));
		when(httpServletRequest.getHeaders(TraceeConstants.HTTP_HEADER_NAME)).thenReturn(headers.elements());
		outInterceptor.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend).putAll(eq(expected));
	}

	@Test
	public void shouldMergeIncomingContextFromCustomHeader() throws Exception {
		outInterceptor.setAcceptIncomingContext(true);
		final String incomingHeader = "myHeader";
		outInterceptor.setIncomingHeaderName(incomingHeader);

		final Map<String, String> expected = new HashMap<String, String>();
		expected.put("testkey", "testValue123");

		final Enumeration<String> headers = Utilities.toEnumeration(Arrays.asList(new Gson().toJson(expected)));
		when(httpServletRequest.getHeaders(incomingHeader)).thenReturn(headers);
		outInterceptor.preHandle(httpServletRequest, httpServletResponse, new Object());
		verify(mockedBackend).putAll(eq(expected));
	}

	@Test
	public void shouldCleanupAfterRequest() throws Exception {
		outInterceptor.afterCompletion(httpServletRequest, httpServletResponse, new Object(), null);
		verify(mockedBackend).clear();
	}

	private TraceeBackend mockedBackend() {
		final TraceeBackend backend = mock(TraceeBackend.class);
		TestLogBackend testLogBackend = new TestLogBackend();
		when(backend.getLoggerFactory()).thenReturn(testLogBackend);
		return backend;
	}
}
