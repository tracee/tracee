package io.tracee.binding.springws.itest;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import io.tracee.binding.springws.itest.testservice.JaxwsTestserviceEndpoint;
import io.tracee.testhelper.PortUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;
import javax.xml.ws.Endpoint;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:jaxWsServer.xml")
public class SpringWsClientToJaxWsServerIT {

	public static final String ENDPOINT_URL = "http://127.0.0.1:" + PortUtil.randomTestPort() + "/jaxws/TestserviceEndpoint";
	private Endpoint endpoint;

	@Autowired
	private TraceeSpringWsClient wsClient;

	@Before
	public void before() {
		endpoint = Endpoint.publish(ENDPOINT_URL, new JaxwsTestserviceEndpoint());
	}

	@After
	public void after() {
		endpoint.stop();
	}

	@PostConstruct
	public void prepareTestClient() {
		wsClient.setDefaultUri(ENDPOINT_URL);
	}

	@Test
	public void callRemoteMethodWithoutInvocationId() throws Exception {
		Tracee.getBackend().remove(TraceeConstants.INVOCATION_ID_KEY);
		final String remoteInvocationId = wsClient.getCurrentTraceeContext();
		assertThat(remoteInvocationId, not(isEmptyOrNullString()));
		assertThat(Tracee.getBackend().get(TraceeConstants.INVOCATION_ID_KEY), is(remoteInvocationId));
	}

	@Test
	public void callRemoteMethodWithInvocationId() throws Exception {
		final String testInvocationId = "tolleId";
		Tracee.getBackend().put(TraceeConstants.INVOCATION_ID_KEY, testInvocationId);
		final String remoteInvocationId = wsClient.getCurrentTraceeContext();
		assertThat(remoteInvocationId, is(testInvocationId));
		assertThat(Tracee.getBackend().get(TraceeConstants.INVOCATION_ID_KEY), is(testInvocationId));
	}

	@Test
	public void callShouldReturnValuesSetInRemoteEndpoint() throws Exception {
		Tracee.getBackend().remove("testId");
		wsClient.getCurrentTraceeContext();
		assertThat(Tracee.getBackend().copyToMap(), hasEntry("testId", "TestValueFromRemoteEndpoint"));
	}
}
