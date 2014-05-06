package io.tracee.contextlogger.connector;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;

public class SimpleAsyncHttpProvider implements AsyncHttpClientProvider {
	@Override
	public final AsyncHttpClient provideHttpClient(AsyncHttpClientConfig config) {
		return new AsyncHttpClient(config);
	}
}
