package io.tracee.binding.jms;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.AsyncDispatch;

public class TraceeMessageProducer implements MessageProducer {


    private final MessageProducer delegate;
	private final TraceeBackend backend;

    TraceeMessageProducer(MessageProducer delegate, TraceeBackend backend) {
        this.delegate = delegate;
		this.backend = backend;
	}

	public TraceeMessageProducer(MessageProducer delegate) {
		this.delegate = delegate;
		this.backend = Tracee.getBackend();
	}

    /**
     * Writes the current TraceeContext to the given javaee message.
     * This method is idempotent.
     */
    protected void writeTraceeContextToMessage(Message message) throws JMSException {

		if (!backend.isEmpty() && backend.getConfiguration().shouldProcessContext(AsyncDispatch)) {
			final Map<String, String> filteredContext = backend.getConfiguration().filterDeniedParams(backend.copyToMap(), AsyncDispatch);
			message.setObjectProperty(TraceeConstants.TPIC_HEADER, filteredContext);
		}
    }

    @Override
    public void send(Message message) throws JMSException {
        writeTraceeContextToMessage(message);
        delegate.send(message);
    }

    @Override
    public void send(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
        writeTraceeContextToMessage(message);
        delegate.send(message, deliveryMode, priority, timeToLive);
    }

    @Override
    public void send(Destination destination, Message message) throws JMSException {
        writeTraceeContextToMessage(message);
        delegate.send(destination, message);
    }

    @Override
    public void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
        writeTraceeContextToMessage(message);
        delegate.send(destination, message, deliveryMode, priority, timeToLive);
    }

    @Override
    public void setDisableMessageID(boolean value) throws JMSException {
        delegate.setDisableMessageID(value);
    }

    @Override
    public boolean getDisableMessageID() throws JMSException {
        return delegate.getDisableMessageID();
    }

    @Override
    public void setDisableMessageTimestamp(boolean value) throws JMSException {
        delegate.setDisableMessageTimestamp(value);
    }

    @Override
    public boolean getDisableMessageTimestamp() throws JMSException {
        return delegate.getDisableMessageTimestamp();
    }

    @Override
    public void setDeliveryMode(int deliveryMode) throws JMSException {
        delegate.setDeliveryMode(deliveryMode);
    }

    @Override
    public int getDeliveryMode() throws JMSException {
        return delegate.getDeliveryMode();
    }

    @Override
    public void setPriority(int defaultPriority) throws JMSException {
        delegate.setPriority(defaultPriority);
    }

    @Override
    public int getPriority() throws JMSException {
        return delegate.getPriority();
    }

    @Override
    public void setTimeToLive(long timeToLive) throws JMSException {
        delegate.setTimeToLive(timeToLive);
    }

    @Override
    public long getTimeToLive() throws JMSException {
        return delegate.getTimeToLive();
    }

    @Override
    public Destination getDestination() throws JMSException {
        return delegate.getDestination();
    }

    @Override
    public void close() throws JMSException {
        delegate.close();
    }

}
