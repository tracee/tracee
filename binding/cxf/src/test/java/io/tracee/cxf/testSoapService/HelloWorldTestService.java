package io.tracee.cxf.testSoapService;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(name = "HelloWorldBinding", portName = "HelloWorldBinding",
		serviceName = "HelloWorldService")
public interface HelloWorldTestService {

	public static final String NAMESPACE = "https://github.com/tracee/tracee";

	@WebResult(partName = "greeting")
	public String sayHelloWorld(
			@WebParam(partName = "firstName") String firstName);
}
