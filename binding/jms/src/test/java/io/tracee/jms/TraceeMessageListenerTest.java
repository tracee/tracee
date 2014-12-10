package io.tracee.jms;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeConstants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.ejb.EJB;
import javax.interceptor.InvocationContext;
import javax.jms.Message;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TraceeMessageListenerTest {


	private final SimpleTraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
	private final TraceeMessageListener unit = new TraceeMessageListener(backend);
	private final InvocationContext invocationContext = mock(InvocationContext.class);
	private final Message message = mock(Message.class);
	private final Map<String,String> encodedContext = new HashMap<String, String>();

	@Before
	public void setupMocks() throws Exception {
		when(invocationContext.getMethod()).thenReturn(MdbLike.class.getMethod("onMessage", Message.class));
		when(invocationContext.getParameters()).thenReturn(new Message[]{message});
		when(invocationContext.proceed()).then(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				new MdbLike().onMessage(message);
				return null;
			}
		});
		when(message.getObjectProperty(TraceeConstants.JMS_HEADER_NAME)).thenReturn(encodedContext);
	}

	@Test
	public void testDecodesFromMessage() throws Exception {
		encodedContext.put("contextFromMessage","yes");
		unit.intercept(invocationContext);
		assertThat(backend.getValuesBeforeLastClear(), hasEntry("contextFromMessage", "yes"));
	}

	@Test
	public void testCleansUpContextAfterProcessing() throws Exception {
		encodedContext.put("contextFromMessage","yes");
		unit.intercept(invocationContext);
		assertThat(backend.entrySet(), is(empty()));
	}


	@EJB
	private class MdbLike {
		public void onMessage(Message message) {
			assertThat(backend, hasEntry("contextFromMessage", "yes"));
		}
	}


}
