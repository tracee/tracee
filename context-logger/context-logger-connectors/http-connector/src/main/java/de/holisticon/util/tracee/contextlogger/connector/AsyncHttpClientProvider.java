package de.holisticon.util.tracee.contextlogger.connector;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public interface AsyncHttpClientProvider {

	AsyncHttpClient provideHttpClient(AsyncHttpClientConfig config);

}
