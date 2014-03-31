package de.holisticon.util.tracee.contextlogger.connector;

import com.ning.http.client.AsyncHttpClientConfig;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.Executors;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class SimpleAsyncHttpProviderTest {

	private final SimpleAsyncHttpProvider unit = new SimpleAsyncHttpProvider();
	private final AsyncHttpClientConfig config = Mockito.mock(AsyncHttpClientConfig.class);

	@Test
	public void testProvideAsyncHttpProvider() {
		when(config.executorService()).thenReturn(Executors.newCachedThreadPool());
		when(config.getIoThreadMultiplier()).thenReturn(1);
		assertThat(unit.provideHttpClient(config), notNullValue());
	}

}
