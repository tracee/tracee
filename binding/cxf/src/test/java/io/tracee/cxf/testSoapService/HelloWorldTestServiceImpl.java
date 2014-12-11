package io.tracee.cxf.testSoapService;


import io.tracee.Tracee;
import io.tracee.TraceeConstants;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

public class HelloWorldTestServiceImpl implements HelloWorldTestService {

	public String sayHelloWorld(String firstName) {
		return "Hello " + firstName + " || requestId was " + Tracee.getBackend().get(TraceeConstants.REQUEST_ID_KEY);
	}
}
