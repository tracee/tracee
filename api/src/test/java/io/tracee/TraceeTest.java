package io.tracee;

import io.tracee.spi.TraceeBackendProvider;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class TraceeTest {

	@Test
	public void constructorOfTraceeShouldBePrivate() throws Exception {
		Constructor<Tracee> constructor = Tracee.class.getDeclaredConstructor();
		assertThat(Modifier.isPrivate(constructor.getModifiers()), is(true));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test(expected = TraceeException.class)
	public void backendRetrievalShouldThrowTraceeExceptionWithDefaultResolver() {
		try {
			Tracee.getBackend();
			fail();
		} catch (TraceeException e) {
			assertThat(e.getMessage(), equalTo("Unable to find a TracEE backend provider. Make sure that you have tracee-core (for slf4j) or any other backend implementation on the classpath."));
			throw e;
		}
	}

	@Test(expected = TraceeException.class)
	public void backendRetrievalShouldWrapRuntimeExceptionIfItOccurs() {
		try {
			final BackendProviderResolver testBackendProvider = Mockito.mock(BackendProviderResolver.class);
			when(testBackendProvider.getBackendProviders()).thenThrow(new RuntimeException());
			Tracee.getBackend(testBackendProvider);
			fail();
		} catch (RuntimeException e) {
			assertThat(e.getMessage(), equalTo("Unable to load available backend providers"));
			throw e;
		}
	}

	@Test(expected = TraceeException.class)
	public void backendRetrievalShouldThrowExceptionWithoutProviders() {
		final BackendProviderResolver resolver = createTestBackendResolverWith(new HashSet<TraceeBackendProvider>());
		try {
			Tracee.getBackend(resolver);
			fail();
		} catch (TraceeException e) {
			assertThat(e.getMessage(), equalTo("Unable to find a TracEE backend provider. Make sure that you have tracee-core (for slf4j) or any other backend implementation on the classpath."));
			throw e;
		}
	}

	@Test(expected = TraceeException.class)
	public void backendRetrievalShouldThrowExceptionWithMoreThenOneProvider() {
		final Set<TraceeBackendProvider> backendProvider = new HashSet<>();
		backendProvider.add(new TestBackendProvider());
		backendProvider.add(new TestBackendProvider());

		final BackendProviderResolver resolver = createTestBackendResolverWith(backendProvider);
		try {
			Tracee.getBackend(resolver);
		} catch (TraceeException e) {
			assertThat(e.getMessage(), allOf(startsWith("Multiple TracEE backend providers found. Don't know which one of the following to use:"),
				containsString(TestBackendProvider.class.getSimpleName())));
			throw e;
		}
	}

	@Test
	public void backendRetrievalShouldReturnBackendWithOneGivenProvider() {
		final Set<TraceeBackendProvider> backendProvider = new HashSet<>();
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
