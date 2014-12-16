package io.tracee.cxf.testSoapService;


import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldTestServiceImpl implements HelloWorldTestService {
	private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldTestServiceImpl.class);
	public String sayHelloWorld(String firstName) {
		Tracee.getBackend().put(TEST_KEY, "accepted");
		LOGGER.info("sayHelloWorld called with firstname '{}'", firstName);
		return "Hello " + firstName + " || requestId was " + Tracee.getBackend().get(TraceeConstants.REQUEST_ID_KEY);
	}
}
