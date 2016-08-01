package io.tracee.binding.springjms;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public class TraceeMessageListenerWrapper implements MessageListener {

	private MessageListener delegate;

	private TraceeBackend backend;

	public TraceeMessageListenerWrapper(MessageListener delegate) {
		this.delegate = delegate;
		this.backend = Tracee.getBackend();
	}

	public TraceeMessageListenerWrapper(MessageListener delegate, TraceeBackend backend) {
		this.delegate = delegate;
		this.backend = backend;
	}

	@Override
	public void onMessage(Message message) {
		try {
			JmsHelper.readTpicFromMessage(message);
			delegate.onMessage(message);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		} finally {
			backend.clear();
		}
	}
}
