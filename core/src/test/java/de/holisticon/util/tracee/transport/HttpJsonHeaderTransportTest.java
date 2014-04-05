package de.holisticon.util.tracee.transport;

import de.holisticon.util.tracee.TraceeLoggerFactory;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

/**
 * @author Sven Bunge (Holisticon AG)
 */
public class HttpJsonHeaderTransportTest {

	private HttpJsonHeaderTransport transport = new HttpJsonHeaderTransport(mock(TraceeLoggerFactory.class));

	@Test
	public void dontRenderEmptyMap() {
		String renderedMap = transport.render(Collections.<String, String>emptyMap());
		assertThat(renderedMap, is(nullValue()));
	}

	@Test
	public void renderMapToString() {
		final Map<String, String> testMap = new HashMap<String, String>();
		testMap.put("keyA", "valueA");
		testMap.put("keyB", "valueB");
		assertThat(transport.render(testMap), equalTo("{\"keyA\":\"valueA\",\"keyB\":\"valueB\"}"));
	}

	@Test
	public void parseEmptyJsonToNull() {
		assertThat(transport.parse(""), is(nullValue()));
	}

	@Test
	public void parseJsonToMap() {
		final String testJson = "{\"keyA\":\"valueA\",\"keyB\":\"valueB\"}";
		final Map<String, String> assertMap = new HashMap<String, String>();
		assertMap.put("keyA", "valueA");
		assertMap.put("keyB", "valueB");
		assertThat(transport.parse(testJson), equalTo(assertMap));
	}
}
