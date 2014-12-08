package io.tracee.examples.webapp;

import io.tracee.examples.jaxws.client.testclient.TraceeJaxWsTestService;
import io.tracee.examples.jaxws.client.testclient.TraceeJaxWsTestWS;
import io.tracee.jaxws.client.TraceeClientHandlerResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.net.MalformedURLException;
import java.net.URL;

@ManagedBean
@RequestScoped
public class TestWebappController {

	public static final String EXAMPLE_WS_URL_PROPERTY_NAME = "tracee.example.ws.url";
	public static final String TEST_SOAP_URL = System.getProperty(EXAMPLE_WS_URL_PROPERTY_NAME, "http://localhost:9080/tracee-examples-jaxws-service/TraceeJaxWsTestService?wsdl");
	static final Logger LOGGER = LoggerFactory.getLogger(TestWebappController.class);

	@ManagedProperty(value = "#{payload}")
	private TestWebappPayload payload;

	public void setPayload(TestWebappPayload payload) {
		this.payload = payload;
	}

	public String multiply() {
		LOGGER.info("multiply");
		if (payload.getFirstArgument() != null && payload.getSecondArgument() != null) {
			payload.setResult(payload.getFirstArgument() * payload.getSecondArgument());
		}
		return null;
	}

	public String sum() {
		LOGGER.info("summarize");
		if (payload.getFirstArgument() != null && payload.getSecondArgument() != null) {
			payload.setResult(payload.getFirstArgument() + payload.getSecondArgument());
		}
		return null;
	}

	public String multiplyJaxWsRemotely() {
		LOGGER.info("call multiply on remote jaxws service method");
		if (payload.getFirstArgument() != null && payload.getSecondArgument() != null) {
			payload.setResult(getJaxWsServiceClient().multiply(payload.getFirstArgument(), payload.getSecondArgument()));
		}
		return null;
	}

	public String sumJaxWsRemotely() {
		LOGGER.info("call sum remote on jaxws service method");
		if (payload.getFirstArgument() != null && payload.getSecondArgument() != null) {
			payload.setResult(getJaxWsServiceClient().sum(payload.getFirstArgument(), payload.getSecondArgument()));
		}
		return null;
	}

	public String multiplyWithJmsJaxWsRemotely() {
		LOGGER.info("call multiply on remote jaxws service method");
		if (payload.getFirstArgument() != null && payload.getSecondArgument() != null) {
			payload.setResult(getJaxWsServiceClient().multiplyWithJms(payload.getFirstArgument(), payload.getSecondArgument()));
		}
		return null;
	}

	public String sumWithJmsJaxWsRemotely() {
		LOGGER.info("call sum remote on jaxws service method");
		if (payload.getFirstArgument() != null && payload.getSecondArgument() != null) {
			payload.setResult(getJaxWsServiceClient().sumWithJms(payload.getFirstArgument(), payload.getSecondArgument()));
		}
		return null;
	}

	public String triggerJaxWsRemoteError() {
		LOGGER.info("Trigger jaxws remote error");
		getJaxWsServiceClient().error(this.payload.getFirstArgument(), this.payload.getSecondArgument());
		return null;
	}

	private TraceeJaxWsTestWS getJaxWsServiceClient() {
		try {
			TraceeJaxWsTestService testWebservice = new TraceeJaxWsTestService(new URL(TEST_SOAP_URL));
			/* ADD TRACEE TO THE CALL */
			testWebservice.setHandlerResolver(new TraceeClientHandlerResolver());
			/* END OF TRACEE SPECIFIC CODE */
			return testWebservice.getPort(TraceeJaxWsTestWS.class);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
}
