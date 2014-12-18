> This document contains documentation for the tracee-jms module. Click [here](/README.md) to get an overview that TracEE is about.

# tracee-jms

> This module can be used to add TracEE context propagation support to JMS MessageProducer and MessageListener beans.


Please add the following dependencies to enable TracEE's JMS support. For example in maven-style projects add to the pom.xml:

```xml
<dependencies>
    ...
    <dependency>
        <groupId>io.tracee</groupId>
        <artifactId>tracee-jms</artifactId>
        <version>RELEASE</version>
    </dependency>
    ...
</dependencies>
```

## Using MessageProducer
You can use the TracEE context propagation for JMS by wrapping the MessageProducer with TraceeMessageWriter.wrap(messageProdecer).

```java
import io.tracee.jms.TraceeMessageWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.*;

@Stateless
public class MessageProducerImpl implements MessageProducer {

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
	    
		  // wrap the producer to enable TracEE
		  final javax.jms.MessageProducer producer = TraceeMessageWriter.wrap(session.createProducer(destination));
		  
		  final TextMessage textMessage = session.createTextMessage();
		  textMessage.setText(message);
		  producer.send(textMessage);
		} catch (JMSException jmse) {
			// handle exception ...
		}
	}
	
}

```

## Using MessageListerner
You only have to add the TraceeMessageListener interceptor to your MesssageDriven bean:

```java
import io.tracee.jms.TraceeMessageListener;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.interceptor.Interceptors;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(
				propertyName = "destination", propertyValue = "exampleQueue")})
@Interceptors({TraceeMessageListener.class})
public class MessageQueueListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		// .. 
	}

}
```

## Background
To enable TracEE's context information in JMS, the MessageProducer must add TracEE's contextual information to the message before it gets persisted. Therefore TracEE offers the TraceeMessageWriter that wraps the MessageProducer and provides those informations before they will be scheduled to a queue or topic.

Thee MessageListeners need to extract those contextual information before the Message is going to be processed.
This can be done easily done by using the TraceeMessageListener interceptor.
