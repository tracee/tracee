package io.tracee.binding.cxf.interceptor;

import io.tracee.testhelper.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class IncomingRequestMessageTest {

	private static final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();

	private TraceeRequestInInterceptor unit = new TraceeRequestInInterceptor(backend);

	private final MessageImpl message = spy(new MessageImpl());

	@Before
	public void onSetup() throws Exception {
		backend.clear();

		when(message.getExchange()).thenReturn(mock(Exchange.class));
		when(message.getExchange().get(eq(Message.REST_MESSAGE))).thenReturn(Boolean.TRUE);
	}

	@Test
	public void shouldHandleMessageWithoutHeader() {
		unit.handleMessage(message);
	}

	@Test
	public void shouldHandleMessageWithoutTraceeHeader() {
		final Map<String, List<String>> headers = new HashMap<>();
		final String context = "myContext";
		headers.put(TraceeConstants.TPIC_HEADER, Collections.singletonList(context));
		message.put(Message.PROTOCOL_HEADERS, headers);
		unit.handleMessage(message);
		assertThat(backend.copyToMap(), hasKey(TraceeConstants.INVOCATION_ID_KEY));
	}

	@Test
	public void generateInvocationIdUponRequest() {
		unit.handleMessage(message);
		assertThat(backend.copyToMap(), hasKey(TraceeConstants.INVOCATION_ID_KEY));
	}

	@Test
	public void onIncomingRequestMessagesIamNotTheRequestor() {
		when(message.get(eq(Message.REQUESTOR_ROLE))).thenReturn(Boolean.FALSE);
		assertThat(unit.shouldHandleMessage(message), is(true));
	}
}
