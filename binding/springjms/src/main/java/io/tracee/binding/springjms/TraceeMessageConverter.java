package io.tracee.binding.springjms;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public class TraceeMessageConverter implements MessageConverter {

	private final MessageConverter delegate;

	private final TraceeBackend backend;

	public TraceeMessageConverter() {
		this.delegate = new SimpleMessageConverter();
		this.backend = Tracee.getBackend();
	}

	public TraceeMessageConverter(MessageConverter delegate) {
		this.delegate = delegate;
		this.backend = Tracee.getBackend();
	}

	TraceeMessageConverter(MessageConverter converter, TraceeBackend backend) {
		this.delegate = converter;
		this.backend = backend;
	}

	@Override
	public Message toMessage(Object o, Session session) throws JMSException, MessageConversionException {
		final Message message = delegate.toMessage(o, session);

		JmsHelper.writeTpicToMessage(message, backend);

		return message;
	}

	@Override
	public Object fromMessage(Message message) throws JMSException, MessageConversionException {

		// Spring doesn't offer a whole transaction where we can hook into. So let's clean before the next message an hope, that the cached
		// consumer holds the thread. Otherwise the TPIC is used for other processing. - But I've not idea how we could avoid this.
		backend.clear();

		JmsHelper.readTpicFromMessage(message, backend);

		return delegate.fromMessage(message);
	}
}
