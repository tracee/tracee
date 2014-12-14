package io.tracee.cxf.interceptor;

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
import static org.hamcrest.Matchers.is;

public class IncomingRequestMessageTest {

	private AbstractTraceeInInterceptor inInterceptor;

	private static final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();

	private final MessageImpl message = new MessageImpl();

	@Before
	public void onSetup() throws Exception {
		backend.clear();
		inInterceptor = new TraceeRequestInInterceptor(backend);
	}

	@Test
	public void shouldHandleMessageWithoutHeader() {
		inInterceptor.handleMessage(message);
	}

	@Test
	public void shouldHandleMessageWithoutTraceeHeader() {
		final Map<String, List<String>> headers = new HashMap<String, List<String>>();
		final String context = "myContext";
		headers.put(TraceeConstants.HTTP_HEADER_NAME, Arrays.asList(context));
		message.put(Message.PROTOCOL_HEADERS, headers);
		inInterceptor.handleMessage(message);
		assertThat(backend.size(), is(0));
	}
}
