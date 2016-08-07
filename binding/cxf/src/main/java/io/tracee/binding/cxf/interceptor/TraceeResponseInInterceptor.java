package io.tracee.binding.cxf.interceptor;

import io.tracee.TraceeBackend;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.Phase;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingResponse;

public class TraceeResponseInInterceptor extends AbstractTraceeInInterceptor {


	public TraceeResponseInInterceptor(TraceeBackend backend, TraceeFilterConfiguration filterConfiguration) {
		super(Phase.PRE_INVOKE, IncomingResponse, backend, filterConfiguration);
	}

	@Override
	protected boolean shouldHandleMessage(Message message) {
		return MessageUtils.isRequestor(message);
	}
}
