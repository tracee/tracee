package de.holisticon.util.tracee.examples.jms;

import javax.ejb.Local;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
@Local
public interface MessageProducer {


	void sendToQueue(String message);
	void sendToTopic(String message);

}
