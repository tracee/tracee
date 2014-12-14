package io.tracee.cxf.interceptor;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.Phase;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;

public class TraceeResponseOutInterceptor extends AbstractTraceeOutInterceptor {

	public TraceeResponseOutInterceptor() {
		this(Tracee.getBackend(), null);
	}

	public TraceeResponseOutInterceptor(TraceeBackend backend) {
		this(backend, null);
	}

	public TraceeResponseOutInterceptor(TraceeBackend backend, String profile) {
		super(Phase.USER_LOGICAL, OutgoingResponse, backend, profile);
	}

	@Override
	protected boolean shouldHandleMessage(Message message) {
		return !MessageUtils.isRequestor(message);
	}
}
