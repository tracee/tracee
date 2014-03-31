package de.holisticon.util.tracee.transport;

import de.holisticon.util.tracee.NoopTraceeLoggerFactory;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class HttpJsonHeaderTransportTest {

	private final HttpJsonHeaderTransport unit = new HttpJsonHeaderTransport(new NoopTraceeLoggerFactory());


	@Test
	public void testParse() throws Exception {
		assertThat(unit.parse("{ \"foo\":\"bar\" }"), equalTo(Collections.singletonMap("foo", "bar")));
	}

	@Test
	public void testRender() throws Exception {

		assertThat(unit.render(Collections.singletonMap("foo","bar")), equalTo("{\"foo\":\"bar\"}"));

	}
}
