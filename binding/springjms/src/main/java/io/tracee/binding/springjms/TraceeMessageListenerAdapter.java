package io.tracee.binding.springjms;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public class TraceeMessageListenerAdapter extends MessageListenerAdapter {

	private TraceeBackend backend;

	public TraceeMessageListenerAdapter() {
		this.backend = Tracee.getBackend();
	}

	public TraceeMessageListenerAdapter(Object delegate) {
		super(delegate);
		this.backend = Tracee.getBackend();
	}

	public TraceeMessageListenerAdapter(TraceeBackend backend) {
		this.backend = backend;
	}

	public TraceeMessageListenerAdapter(Object delegate, TraceeBackend backend) {
		super(delegate);
		this.backend = backend;
	}

	@Override
	public void onMessage(Message message, Session session) throws JMSException {
		try {
			JmsHelper.readTpicFromMessage(message);
			super.onMessage(message, session);
		} finally {
			backend.clear();
		}
	}
}
