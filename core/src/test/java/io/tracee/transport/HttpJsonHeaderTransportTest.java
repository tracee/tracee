package io.tracee.transport;

import io.tracee.TraceeLogger;
import io.tracee.TraceeLoggerFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;

public class HttpJsonHeaderTransportTest {

	private final TraceeLoggerFactory traceeLoggerFactoryMock = mock(TraceeLoggerFactory.class);
	private final TraceeLogger traceeLoggerMock = mock(TraceeLogger.class);
	private HttpJsonHeaderTransport transport;

	@Before
	public void setUpMocks() {
		when(traceeLoggerFactoryMock.getLogger(eq(HttpJsonHeaderTransport.class))).thenReturn(traceeLoggerMock);
		transport =  new HttpJsonHeaderTransport(traceeLoggerFactoryMock);
	}

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

	@Test
	public void testParseNonJsonReturnsEmptyMap() {
		final String brokenJson = "a:b";
		assertThat(transport.parse(brokenJson).entrySet(), empty());
	}
}
