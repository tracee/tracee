package io.tracee.binding.jms;

import io.tracee.TraceeBackend;
import io.tracee.configuration.TraceeFilterConfiguration;

import javax.jms.MessageProducer;
import javax.jms.QueueSender;
import javax.jms.TopicPublisher;

public final class TraceeMessageWriter {

    private TraceeMessageWriter() {
    }

    public static MessageProducer wrap(MessageProducer messageProducer, TraceeBackend backend, TraceeFilterConfiguration filterConfiguration) {
        return new TraceeMessageProducer(messageProducer, backend, filterConfiguration);
    }

    public static QueueSender wrap(QueueSender queueSender, TraceeBackend backend, TraceeFilterConfiguration filterConfiguration) {
        return new TraceeQueueSender(new TraceeMessageProducer(queueSender, backend, filterConfiguration), queueSender);
    }

    public static TopicPublisher wrap(TopicPublisher topicPublisher, TraceeBackend backend, TraceeFilterConfiguration filterConfiguration) {
        return new TraceeTopicPublisher(new TraceeMessageProducer(topicPublisher, backend, filterConfiguration), topicPublisher);
    }
}
