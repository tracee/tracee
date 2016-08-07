package io.tracee.binding.jms;

import io.tracee.TraceeBackend;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.DelegationTestUtil;
import org.junit.Test;
import org.mockito.Mockito;

import javax.jms.MessageProducer;
import javax.jms.QueueSender;
import javax.jms.TopicPublisher;

import static org.mockito.Mockito.mock;

public class MessageDelegationTest {

	private final TraceeBackend backend = Mockito.mock(TraceeBackend.class);
	private final TraceeFilterConfiguration filterConfiguration = Mockito.mock(TraceeFilterConfiguration.class);

	@Test
	public void delegateTraceeMessageProducerToMessageProducer() {
		final MessageProducer messageProducer = mock(MessageProducer.class);
		DelegationTestUtil.assertDelegationToSpy(messageProducer).by(new TraceeMessageProducer(messageProducer, backend, filterConfiguration)).verify();
	}

	@Test
	public void delegateTraceeMessageProducerToMessageQueueSender() {
		final QueueSender queueSender = mock(QueueSender.class);
		final TraceeMessageProducer traceeMessageProducer = new TraceeMessageProducer(queueSender, backend, filterConfiguration);
		DelegationTestUtil.assertDelegationToSpy(queueSender).by(new TraceeQueueSender(traceeMessageProducer, queueSender)).verify();
	}

	@Test
	public void delegateTraceeTopicPublisherToTopicPublisher() {
		final TopicPublisher topicPublisher = mock(TopicPublisher.class);
		final TraceeTopicPublisher unit = new TraceeTopicPublisher(new TraceeMessageProducer(topicPublisher, backend, filterConfiguration), topicPublisher);
		DelegationTestUtil.assertDelegationToSpy(topicPublisher).by(unit).verify();
	}
}
