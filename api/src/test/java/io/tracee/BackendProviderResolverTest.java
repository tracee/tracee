package io.tracee;

import io.tracee.spi.TraceeBackendProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;

import static io.tracee.BackendProviderResolver.EmptyBackendProviderSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

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
}
