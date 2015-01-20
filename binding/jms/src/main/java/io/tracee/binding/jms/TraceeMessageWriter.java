package io.tracee.binding.jms;

import javax.jms.MessageProducer;
import javax.jms.QueueSender;
import javax.jms.TopicPublisher;

public final class TraceeMessageWriter {

    private TraceeMessageWriter() {
    }

    public static MessageProducer wrap(MessageProducer messageProducer) {
        return new TraceeMessageProducer(messageProducer);
    }

    public static QueueSender wrap(QueueSender queueSender) {
        return new TraceeQueueSender(new TraceeMessageProducer(queueSender), queueSender);
    }

    public static TopicPublisher wrap(TopicPublisher topicPublisher) {
        return new TraceeTopicPublisher(new TraceeMessageProducer(topicPublisher), topicPublisher);
    }

}
