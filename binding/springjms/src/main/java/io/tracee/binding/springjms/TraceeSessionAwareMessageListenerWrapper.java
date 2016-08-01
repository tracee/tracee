package io.tracee.binding.springjms;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import org.springframework.jms.listener.SessionAwareMessageListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public class TraceeSessionAwareMessageListenerWrapper<M extends Message> implements SessionAwareMessageListener<M> {

	private SessionAwareMessageListener<M> delegate;

	private TraceeBackend backend;

	public TraceeSessionAwareMessageListenerWrapper(SessionAwareMessageListener<M> delegate) {
		this.delegate = delegate;
		this.backend = Tracee.getBackend();
	}

	public TraceeSessionAwareMessageListenerWrapper(SessionAwareMessageListener<M> delegate, TraceeBackend backend) {
		this.delegate = delegate;
		this.backend = backend;
	}

	@Override
	public void onMessage(M message, Session session) throws JMSException {
		try {
			JmsHelper.readTpicFromMessage(message);
			delegate.onMessage(message, session);
		} finally {
			backend.clear();
		}
	}
}
