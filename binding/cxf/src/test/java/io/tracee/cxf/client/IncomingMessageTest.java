package io.tracee.cxf.client;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.cxf.interceptor.TraceeInInterceptor;
import io.tracee.transport.HttpJsonHeaderTransport;
import io.tracee.transport.TransportSerialization;
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

public class IncomingMessageTest {

	private TraceeInInterceptor inInterceptor;

	private static final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();

	private final MessageImpl message = new MessageImpl();

	private TransportSerialization<String> httpSerializer;

	@Before
	public void onSetup() throws Exception {
		backend.clear();
		inInterceptor = new TraceeInInterceptor(backend);
		httpSerializer = new HttpJsonHeaderTransport(backend.getLoggerFactory());
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
