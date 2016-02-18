package io.tracee.binding.cxf.testSoapService;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(name = "HelloWorldBinding", portName = "HelloWorldBinding",
		serviceName = "HelloWorldService")
public interface HelloWorldTestService {

	String NAMESPACE = "https://github.com/tracee/tracee";

	String TEST_KEY = "serverKey";

	@WebResult(partName = "greeting")
	String sayHelloWorld(
			@WebParam(partName = "firstName") String firstName);
}
