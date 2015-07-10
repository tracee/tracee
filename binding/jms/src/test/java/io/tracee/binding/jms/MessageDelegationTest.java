package io.tracee.binding.jms;

import io.tracee.testhelper.DelegationTestUtil;
import org.junit.Test;

import javax.jms.MessageProducer;
import javax.jms.QueueSender;
import javax.jms.TopicPublisher;

import static org.mockito.Mockito.mock;

public class MessageDelegationTest {

	@Test
	public void delegateTraceeMessageProducerToMessageProducer() {
		final MessageProducer messageProducer = mock(MessageProducer.class);
		DelegationTestUtil.assertDelegationToSpy(messageProducer).by(new TraceeMessageProducer(messageProducer)).verify();
	}

	@Test
	public void delegateTraceeMessageProducerToMessageQueueSender() {
		final QueueSender queueSender = mock(QueueSender.class);
		final TraceeMessageProducer traceeMessageProducer = new TraceeMessageProducer(queueSender);
		DelegationTestUtil.assertDelegationToSpy(queueSender).by(new TraceeQueueSender(traceeMessageProducer, queueSender)).verify();
	}

	@Test
	public void delegateTraceeTopicPublisherToTopicPublisher() {
		final TopicPublisher topicPublisher = mock(TopicPublisher.class);
		final TraceeTopicPublisher unit = new TraceeTopicPublisher(new TraceeMessageProducer(topicPublisher), topicPublisher);
		DelegationTestUtil.assertDelegationToSpy(topicPublisher).by(unit).verify();
	}
}
