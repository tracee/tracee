package io.tracee.binding.springjms;

import io.tracee.Tracee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.HashMap;
import java.util.Map;

import static io.tracee.TraceeConstants.TPIC_HEADER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test-context.xml")
public class TraceeMessageConverterIT {

	private static final String TEST_DESTINATION = "testDestination";

	@Autowired
	private JmsTemplate jmsTemplate;

	@Before
	public void setup() {
		Tracee.getBackend().clear();
	}

	@Test
	public void shouldAddTpicToMessage() throws JMSException {
		Tracee.getBackend().put("integrationTestKey", "ourVal");
		jmsTemplate.convertAndSend(TEST_DESTINATION, "Our test message");

		final Message consumedMessage = jmsTemplate.receive(TEST_DESTINATION);
		@SuppressWarnings("unchecked")
		final Map<String, String> tpicMap = (Map<String, String>) consumedMessage.getObjectProperty(TPIC_HEADER);
		assertThat(tpicMap, hasEntry("integrationTestKey", "ourVal"));
	}

	@Test
	public void shouldReadTpicFromMesage() throws JMSException {
		jmsTemplate.send(TEST_DESTINATION, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				final TextMessage message = session.createTextMessage("Receive this!");
				final Map<String, String> messageTpic = new HashMap<String, String>();
				messageTpic.put("receiveThisKey!", "receiveThisAndDoWhatYouWand!");
				message.setObjectProperty(TPIC_HEADER, messageTpic);
				return message;
			}
		});

		jmsTemplate.receiveAndConvert(TEST_DESTINATION);
		assertThat(Tracee.getBackend().copyToMap(), hasEntry("receiveThisKey!", "receiveThisAndDoWhatYouWand!"));
	}

	@Test
	public void ensureInvocationIdIsCreatedIfNoInvocationIdIsReceived() {
		assertThat(Tracee.getBackend().getInvocationId(), is(nullValue()));

		jmsTemplate.send(TEST_DESTINATION, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("Message without invocation Id");
			}
		});

		final String receivedMessage = (String) jmsTemplate.receiveAndConvert(TEST_DESTINATION);
		assertThat(receivedMessage, is("Message without invocation Id"));

		assertThat(Tracee.getBackend().getInvocationId().length(), is(32));
	}
}
