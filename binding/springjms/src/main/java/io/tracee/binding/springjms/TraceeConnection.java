package io.tracee.binding.springjms;

import javax.jms.*;

/**
 * A delegating {@link Connection},
 * wraps the session created by {@link this#delegate} with a {@link TraceeSession} instance.
 *
 * @see TraceeSession
 */
public class TraceeConnection implements Connection {
    private final Connection delegate;

    public TraceeConnection(Connection delegate) {
        this.delegate = delegate;
    }

    @Override
    public void close() throws JMSException {
        delegate.close();
    }

    @Override
    public void setClientID(String clientID) throws JMSException {
        delegate.setClientID(clientID);
    }

    @Override
    public ExceptionListener getExceptionListener() throws JMSException {
        return delegate.getExceptionListener();
    }

    @Override
    public String getClientID() throws JMSException {
        return delegate.getClientID();
    }

    @Override
    public void start() throws JMSException {
        delegate.start();
    }

    @Override
    public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String subscriptionName, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        return delegate.createDurableConnectionConsumer(topic, subscriptionName, messageSelector, sessionPool, maxMessages);
    }

    @Override
    public void stop() throws JMSException {
        delegate.stop();
    }

    @Override
    public ConnectionMetaData getMetaData() throws JMSException {
        return delegate.getMetaData();
    }

    @Override
    public ConnectionConsumer createConnectionConsumer(Destination destination, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        return delegate.createConnectionConsumer(destination, messageSelector, sessionPool, maxMessages);
    }

    @Override
    public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
        return new TraceeSession(delegate.createSession(transacted, acknowledgeMode));
    }

    @Override
    public void setExceptionListener(ExceptionListener listener) throws JMSException {
        delegate.setExceptionListener(listener);
    }
}
