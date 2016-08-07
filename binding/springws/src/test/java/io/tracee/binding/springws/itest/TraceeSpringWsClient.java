package io.tracee.binding.springws.itest;

import io.tracee.binding.springws.itest.testservice.TraceeEntryMethod;
import io.tracee.binding.springws.itest.testservice.TraceeEntryResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class TraceeSpringWsClient extends WebServiceGatewaySupport {

	public String getCurrentTraceeContext() {
		TraceeEntryResponse response =
			(TraceeEntryResponse) getWebServiceTemplate().marshalSendAndReceive(new TraceeEntryMethod());

		return response.getRemoteInvocationId();
	}
}
