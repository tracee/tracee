package de.holisticon.util.tracee.examples.jms;

import de.holisticon.util.tracee.jms.TraceeMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.interceptor.Interceptors;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
		@ActivationConfigProperty(
				propertyName = "destination", propertyValue = "jms/exampleTopic") })
@Interceptors(TraceeMessageListener.class)
public class MessageTopicListener implements MessageListener {

	private static final Logger LOG = LoggerFactory.getLogger(MessageTopicListener.class);

	@Override
	public void onMessage(Message message) {
		LOG.info("I just received the message \"{}\" on jms/exampleTopic", message);
	}

}
