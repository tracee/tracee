package io.tracee.cxf.client;


import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import io.tracee.cxf.test.HelloWorldPortType;

import javax.jws.WebService;

@WebService(targetNamespace = "https://github.com/tracee/tracee", name = "HelloWorld_PortType", wsdlLocation = "test.wsdl",
		serviceName = "HelloWorld_Service", portName = "HelloWorld_Port")
public class HelloWorldTestService implements HelloWorldPortType {
	@Override
	public String sayHelloWorld(String firstName) {
		return "Hello " + firstName + " || requestId was " + Tracee.getBackend().get(TraceeConstants.REQUEST_ID_KEY);
	}
}
