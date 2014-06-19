package io.tracee.examples.jms;

import io.tracee.jms.TraceeMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.interceptor.Interceptors;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.javaee.Queue"),
		@ActivationConfigProperty(
				propertyName = "destination", propertyValue = "exampleQueue") })
@Interceptors(TraceeMessageListener.class)
public class MessageQueueListener implements MessageListener {

	private static final Logger LOG = LoggerFactory.getLogger(MessageQueueListener.class);

	@Override
	public void onMessage(Message message) {
		LOG.info("I just received the message \"{}\" on javaee/exampleQueue", message);
	}

}
