package io.tracee.binding.cxf.testSoapService;

import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldTestServiceImpl implements HelloWorldTestService {

	private final TraceeBackend backend;

	private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldTestServiceImpl.class);

	public HelloWorldTestServiceImpl(TraceeBackend backend) {
		this.backend = backend;
	}

	public String sayHelloWorld(String firstName) {
		backend.put(TEST_KEY, "accepted");
		LOGGER.info("sayHelloWorld called with firstname '{}'", firstName);
		return "Hello " + firstName + " || requestId was " + backend.get(TraceeConstants.REQUEST_ID_KEY);
	}
}
