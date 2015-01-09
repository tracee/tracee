package io.tracee.binding.cxf.interceptor;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.mockito.Mockito.spy;

public class IncomingRequestMessageTest {

	private AbstractTraceeInInterceptor unit;

	private static final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();

	private final MessageImpl message = spy(new MessageImpl());

	@Before
	public void onSetup() throws Exception {
		backend.clear();
		unit = new TraceeRequestInInterceptor(backend);
	}

	@Test
	public void shouldHandleMessageWithoutHeader() {
		unit.handleMessage(message);
	}

	@Test
	public void shouldHandleMessageWithoutTraceeHeader() {
		final Map<String, List<String>> headers = new HashMap<String, List<String>>();
		final String context = "myContext";
		headers.put(TraceeConstants.HTTP_HEADER_NAME, Arrays.asList(context));
		message.put(Message.PROTOCOL_HEADERS, headers);
		unit.handleMessage(message);
		assertThat(backend.copyToMap(), hasKey(TraceeConstants.REQUEST_ID_KEY));
	}

	@Test
	public void generateRequestIdUponRequest() {
		unit.handleMessage(message);
		assertThat(backend.copyToMap(), hasKey(TraceeConstants.REQUEST_ID_KEY));
	}
}
