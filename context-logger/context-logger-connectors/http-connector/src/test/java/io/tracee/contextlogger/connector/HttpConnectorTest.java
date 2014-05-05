package io.tracee.contextlogger.connector;

import com.ning.http.client.AsyncHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ListenableFuture;
import io.tracee.NoopTraceeLoggerFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by Tobias Gindler, holisticon AG on 20.02.14.
 */
public class HttpConnectorTest {

	private final AsyncHttpClientProvider asyncHttpClientProvider = mock(AsyncHttpClientProvider.class);
	private final AsyncHttpClient asyncHttpClient = mock(AsyncHttpClient.class);
	private final AsyncHttpClient.BoundRequestBuilder requestBuilder = mock(AsyncHttpClient.BoundRequestBuilder.class, withSettings().defaultAnswer(new Answer() {
		@Override
		public Object answer(InvocationOnMock invocation) throws Throwable {
			if (invocation.getMethod().getReturnType().isAssignableFrom(AsyncHttpClient.BoundRequestBuilder.class))
				return requestBuilder;
			else
				return null;
		}
	}));

	private final HttpConnector unit = new HttpConnector(new NoopTraceeLoggerFactory(), asyncHttpClientProvider);

	@Before
	public void setUp() throws IOException {
		when(asyncHttpClientProvider.provideHttpClient(Mockito.any(AsyncHttpClientConfig.class))).thenReturn(asyncHttpClient);
		when(asyncHttpClient.preparePost(Mockito.anyString())).thenReturn(requestBuilder);
		when(requestBuilder.execute(Mockito.any(AsyncHandler.class))).thenReturn(Mockito.mock(ListenableFuture.class));
	}

	@Test
	public void testHttpConnectorPostsReportToTargetUrl() throws IOException {

		final Map<String, String> map = new HashMap<String, String>();
		map.put(HttpConnector.PROPERTY_URL, "https://www.example.com");
		unit.init(map);
		unit.sendErrorReport("{'abc':'def'}");
		verify(asyncHttpClient).preparePost("https://www.example.com");
		verify(requestBuilder).setBody(eq("{'abc':'def'}"));
		verify(requestBuilder).execute(Mockito.any(AsyncHandler.class));
	}

	@Test
	public void testConvertStringToInt() {
		assertThat(unit.convertStringToInt("1", 0), equalTo(1));
	}
	@Test
	public void testConvertStringToIntFallsBackToDefault() {
		assertThat(unit.convertStringToInt("cannot parse", 42), equalTo(42));
	}

}
