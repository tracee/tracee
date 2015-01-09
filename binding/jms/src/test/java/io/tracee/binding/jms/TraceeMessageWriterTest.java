package io.tracee.binding.jms;

import org.junit.Test;

import javax.jms.MessageProducer;
import javax.jms.QueueSender;
import javax.jms.TopicPublisher;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TraceeMessageWriterTest {

	@Test
	public void shouldWrapMessageProducer() throws Exception {
		final MessageProducer producer = mock(MessageProducer.class);
		TraceeMessageWriter.wrap(producer).close();
		verify(producer).close();
	}

	@Test
	public void shouldWrapQueueSender() throws Exception {
		final QueueSender sender = mock(QueueSender.class);
		TraceeMessageWriter.wrap(sender).close();
		verify(sender).close();
	}

	@Test
	public void shouldWrapTopicPublisher() throws Exception {
		final TopicPublisher publisher = mock(TopicPublisher.class);
		TraceeMessageWriter.wrap(publisher).close();
		verify(publisher).close();
	}
}
