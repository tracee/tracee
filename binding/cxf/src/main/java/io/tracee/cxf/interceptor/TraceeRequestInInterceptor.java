package io.tracee.cxf.interceptor;

import io.tracee.TraceeBackend;
import io.tracee.Utilities;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.Phase;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;

public class TraceeRequestInInterceptor extends AbstractTraceeInInterceptor {

	public TraceeRequestInInterceptor(TraceeBackend backend) {
		this(backend, null);
	}

	public TraceeRequestInInterceptor(TraceeBackend backend, String profile) {
		super(Phase.PRE_INVOKE, IncomingRequest, backend, profile);
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		super.handleMessage(message);
		if (shouldHandleMessage(message)) {
			Utilities.generateRequestIdIfNecessary(backend);
		}
	}

	@Override
	protected boolean shouldHandleMessage(Message message) {
		return !MessageUtils.isRequestor(message);
	}
}
