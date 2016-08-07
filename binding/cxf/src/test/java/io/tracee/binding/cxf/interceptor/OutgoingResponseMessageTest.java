package io.tracee.binding.cxf.interceptor;

import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.testhelper.SimpleTraceeBackend;
import io.tracee.transport.HttpHeaderTransport;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class OutgoingResponseMessageTest {

	private static final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();

	private TraceeResponseOutInterceptor outInterceptor;

	private final MessageImpl message = spy(new MessageImpl());

	private HttpHeaderTransport httpSerializer;

	@Before
	public void onSetup() throws Exception {
		backend.clear();
		outInterceptor = new TraceeResponseOutInterceptor(backend);
		httpSerializer = new HttpHeaderTransport();

		when(message.getExchange()).thenReturn(mock(Exchange.class));
		when(message.getExchange().get(eq(Message.REST_MESSAGE))).thenReturn(Boolean.TRUE);
	}

	@Test
	public void renderEmptyContextToResponse() {
		outInterceptor.handleMessage(message);
	}

	@Test
	public void renderContextToResponse() {
		backend.put("myContextKey", "contextValue2");
		outInterceptor.handleMessage(message);
		final Map<Object, List<String>> headers = CastUtils.cast((Map<?, ?>) message.get(Message.PROTOCOL_HEADERS));
		final Map<String, String> traceeContext = httpSerializer.parse(headers.get(TraceeConstants.TPIC_HEADER));
		assertThat(traceeContext.get("myContextKey"), is("contextValue2"));
	}
}
