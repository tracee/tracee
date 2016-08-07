package io.tracee.binding.springmvc;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import org.junit.Test;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static io.tracee.TraceeConstants.INVOCATION_ID_KEY;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TraceeResponseBodyAdviceTest {

	private final TraceeInterceptor interceptor = new TraceeInterceptor();

	private final TraceeResponseBodyAdvice advice = new TraceeResponseBodyAdvice(interceptor);

	@Test
	public void adviceShouldHandleAllKindOfRequests() {
		assertThat(advice.supports(null, null), is(true));
	}

	@Test
	public void shouldAddTraceeHeaderToResponse() {
		Tracee.getBackend().put(INVOCATION_ID_KEY, "mySuperValue");
		final HttpServletRequest request = mock(HttpServletRequest.class);
		final HttpServletResponse response = mock(HttpServletResponse.class);

		advice.beforeBodyWrite(null, null, null, null, new ServletServerHttpRequest(request), new ServletServerHttpResponse(response));
		verify(response).setHeader(eq(TraceeConstants.TPIC_HEADER), eq(INVOCATION_ID_KEY + "=mySuperValue"));
	}

}
