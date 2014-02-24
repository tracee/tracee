package de.holisticon.util.tracee;

import de.holisticon.util.tracee.spi.TraceeBackendProvider;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeTest {

	@Test(expected = TraceeException.class)
	public void backendRetrievalShouldThrowTraceeExceptionWithDefaultResolver() {
		try {
			Tracee.getBackend();
		} catch (TraceeException e) {
			assertThat(e.getMessage(), equalTo("Unable to find a tracee backend provider. Make sure that you have a implementation on your classpath."));
			throw e;
		}
	}

	@Test(expected = TraceeException.class)
	public void backendRetrievalShouldThrowExceptionWithoutProviders() {
		final BackendProviderResolver resolver = createTestBackendResolverWith(new HashSet<TraceeBackendProvider>());
		try {
			Tracee.getBackend(resolver);
		} catch (TraceeException e) {
			assertThat(e.getMessage(), equalTo("Unable to find a tracee backend provider. Make sure that you have a implementation on your classpath."));
			throw e;
		}
	}

	@Test(expected = TraceeException.class)
	public void backendRetrievalShouldThrowExceptionWithMoreThenOneProvider() {
		final Set<TraceeBackendProvider> backendProvider = new HashSet<TraceeBackendProvider>();
		backendProvider.add(new TestBackendProvider());
		backendProvider.add(new TestBackendProvider());

		final BackendProviderResolver resolver = createTestBackendResolverWith(backendProvider);
		try {
			Tracee.getBackend(resolver);
		} catch (TraceeException e) {
			assertThat(e.getMessage(), allOf(startsWith("Multiple context providers found. Don't know which one of the following to use:"),
					containsString(TestBackendProvider.class.getSimpleName())));
			throw e;
		}
	}

	@Test
	public void backendRetrievalShouldReturnBackendWithOneGivenProvider() {
		final Set<TraceeBackendProvider> backendProvider = new HashSet<TraceeBackendProvider>();
		backendProvider.add(new TestBackendProvider());

		final TraceeBackend resolvedBackend = Tracee.getBackend(createTestBackendResolverWith(backendProvider));

		assertThat(resolvedBackend, is(not(nullValue())));
	}

	private BackendProviderResolver createTestBackendResolverWith(Set<TraceeBackendProvider> backendProvider) {
		final BackendProviderResolver testBackendProvider = Mockito.mock(BackendProviderResolver.class);
		when(testBackendProvider.getBackendProviders()).thenReturn(backendProvider);
		return testBackendProvider;
	}

	private static final class TestBackendProvider implements TraceeBackendProvider {
		@Override
		public TraceeBackend provideBackend() {
			return Mockito.mock(TraceeBackend.class);
		}
	}
}
