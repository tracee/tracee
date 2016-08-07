package io.tracee;

import io.tracee.spi.TraceeBackendProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static io.tracee.BackendProviderResolver.EmptyBackendProviderSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BackendProviderResolver.class)
public class BackendProviderResolverTest {

	private BackendProviderResolver out;

	@Before
	public void before() {
		out = new BackendProviderResolver();
	}

	@Test
	public void shouldReturnTrueIfSetIsNull() {
		assertThat(out.isLookupNeeded(null), is(true));
	}

	@Test
	public void shouldReturnTrueIfSetIsEmpty() {
		assertThat(out.isLookupNeeded(Collections.<TraceeBackendProvider>emptySet()), is(true));
	}

	@Test
	public void shouldReturnFalseIfEmptySetObjectIsPresent() {
		assertThat(out.isLookupNeeded(new EmptyBackendProviderSet()), is(false));
	}

	@Test
	public void emptyProviderSetShouldReturnEmptyIterator() {
		final Iterator<TraceeBackendProvider> emptySetIter = new EmptyBackendProviderSet().iterator();
		assertThat(emptySetIter.hasNext(), is(false));
	}

	@Test
	public void emptyProviderSetShouldBeEmpty() {
		assertThat(new EmptyBackendProviderSet(), empty());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void emptyProviderSetShouldBeImmutable() {
		new EmptyBackendProviderSet().add(mock(TraceeBackendProvider.class));
	}

	@Test
	public void shouldReturnClassloaderFromContext() throws Exception {
		final Set<TraceeBackendProvider> contextClassloader = new HashSet<>();
		contextClassloader.add(mock(TraceeBackendProvider.class));

		BackendProviderResolver resolver = PowerMockito.spy(new BackendProviderResolver());

		when(resolver, method(BackendProviderResolver.class, "loadProviders", ClassLoader.class))
			.withArguments(BackendProviderResolver.GetClassLoader.fromContext())
			.thenReturn(contextClassloader);
		assertThat(resolver.getBackendProviders(), is(equalTo(contextClassloader)));
	}
}
