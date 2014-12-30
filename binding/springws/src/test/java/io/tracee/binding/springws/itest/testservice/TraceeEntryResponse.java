package io.tracee.binding.springws.itest.testservice;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = JaxwsTestservice.TNS, name = "currentTraceeContextResponse")
public class TraceeEntryResponse {

	@XmlElement(name = "remoteInvocationId")
	private String remoteInvocationId;

	protected TraceeEntryResponse() {
	}

	public TraceeEntryResponse(String remoteInvocationId) {
		this.remoteInvocationId = remoteInvocationId;
	}

	public String getRemoteInvocationId() {
		return remoteInvocationId;
	}
}
