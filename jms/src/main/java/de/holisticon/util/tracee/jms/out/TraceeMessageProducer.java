package de.holisticon.util.tracee.jms.out;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeMessageProducer implements MessageProducer {


    protected final MessageProducer delegate;

    public TraceeMessageProducer(MessageProducer delegate) {
        this.delegate = delegate;
    }

    /**
     * Writes the current TraceeContext to the given jms message.
     * This method is idempotent.
     */
    protected final void writeTraceeContextToMessage(Message message) throws JMSException {
        final TraceeBackend traceeProperties = Tracee.getBackend();
        final TreeMap<String, String> defensiveCopy = new TreeMap<String, String>(traceeProperties);
        message.setObjectProperty(TraceeConstants.JMS_HEADER_NAME, defensiveCopy);
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
