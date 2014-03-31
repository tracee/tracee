package de.holisticon.util.tracee.contextlogger.connector;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class SimpleAsyncHttpProvider implements AsyncHttpClientProvider {
	@Override
	public AsyncHttpClient provideHttpClient(AsyncHttpClientConfig config) {
		return new AsyncHttpClient(config);
	}
}
