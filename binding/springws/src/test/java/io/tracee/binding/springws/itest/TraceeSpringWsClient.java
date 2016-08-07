package io.tracee.binding.springws.itest;

import io.tracee.binding.springws.itest.testservice.TraceeEntryMethod;
import io.tracee.binding.springws.itest.testservice.TraceeEntryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.WebServiceTemplate;

public class TraceeSpringWsClient {

	@Autowired
	WebServiceTemplate webServiceTemplate;

	public String getCurrentTraceeContext() {
		TraceeEntryResponse response =
			(TraceeEntryResponse) webServiceTemplate.marshalSendAndReceive(new TraceeEntryMethod());

		return response.getRemoteInvocationId();
	}
}
