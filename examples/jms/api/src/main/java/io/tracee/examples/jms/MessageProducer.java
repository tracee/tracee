package io.tracee.examples.jms;

import javax.ejb.Local;

@Local
public interface MessageProducer {


	void sendToQueue(String message);
	void sendToTopic(String message);

}
