package io.tracee.binding.springws.itest.testservice;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class SpringWsTestserviceEndpoint {

	@PayloadRoot(localPart = JaxwsTestservice.CURRENT_TRACEE_CONTEXT_LOCALPART,
		namespace = JaxwsTestservice.TNS)
	@ResponsePayload
	public TraceeEntryResponse currentTraceeContext(@RequestPayload TraceeEntryMethod method) {
		Tracee.getBackend().put("testId", "TestValueFromRemoteEndpoint");
		return new TraceeEntryResponse(Tracee.getBackend().get(TraceeConstants.INVOCATION_ID_KEY));
	}

}
