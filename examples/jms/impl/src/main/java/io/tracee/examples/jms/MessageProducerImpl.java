package io.tracee.examples.jms;

import io.tracee.jms.TraceeMessageWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.*;

@Stateless
public class MessageProducerImpl implements MessageProducer {

	private static final Logger LOG = LoggerFactory.getLogger(MessageProducerImpl.class);

	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Resource(mappedName = "java:/exampleQueue")
	private Queue exampleQueue;

	@Resource(mappedName = "java:/exampleTopic")
	private Topic exampleTopic;

	@Override
	public void sendToQueue(String message) {
		sendToDestination(message, exampleQueue);
	}

	@Override
	public void sendToTopic(String message) {
		sendToDestination(message, exampleTopic);
	}

	private void sendToDestination(String message, Destination destination) {
		Connection connection = null;
		try {
			connection = connectionFactory.createConnection();
			final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			final javax.jms.MessageProducer producer = TraceeMessageWriter.wrap(session.createProducer(destination));
			final TextMessage textMessage = session.createTextMessage();
			textMessage.setText(message);
			LOG.info("I am about to send the message \"{]\" to {}", message, destination.toString());
			producer.send(textMessage);
			session.commit();
		} catch (JMSException jmse) {
			throw new RuntimeException("This example is so cheap, this must not have happened!", jmse);
		}
	}

}
