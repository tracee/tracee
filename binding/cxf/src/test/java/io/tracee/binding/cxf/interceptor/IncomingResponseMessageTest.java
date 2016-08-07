package io.tracee.binding.cxf.interceptor;

import io.tracee.TraceeBackend;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.PermitAllTraceeFilterConfiguration;
import io.tracee.testhelper.SimpleTraceeBackend;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class IncomingResponseMessageTest {

	private static final TraceeBackend backend = new SimpleTraceeBackend();
	private static final TraceeFilterConfiguration filterConfiguration = PermitAllTraceeFilterConfiguration.INSTANCE;

	private TraceeResponseInInterceptor unit = new TraceeResponseInInterceptor(backend, filterConfiguration);

	private final MessageImpl message = spy(new MessageImpl());

	@Before
	public void before() {
		backend.clear();
	}

	@Test
	public void onIncomingResponseMessagesIamTheRequestor() {
		when(message.get(eq(Message.REQUESTOR_ROLE))).thenReturn(Boolean.TRUE);
		assertThat(unit.shouldHandleMessage(message), is(true));
	}
}
