package de.holisticon.util.tracee.outbound.httpclient;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeHttpClientIT {

	private final HttpClient delegateMock = Mockito.mock(HttpClient.class);

	@Test
	@Ignore("To be written")
	public void test() {
		final HttpClient httpClient = TraceeHttpClientDecorator.wrap(new HttpClient());

		//TODO: to be written
		GetMethod getMethod = new GetMethod("http://localhost:port");
	}

}
