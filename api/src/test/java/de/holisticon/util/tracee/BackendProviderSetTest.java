package de.holisticon.util.tracee;

import de.holisticon.util.tracee.spi.TraceeBackendProvider;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.Sets;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.ref.SoftReference;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;

/**
 * @author Sven Bunge, Holisticon AG
 */
@RunWith(PowerMockRunner.class)
public class BackendProviderSetTest {
	
	@Test
	public void shouldReturnSize() {
		final Set<TraceeBackendProvider> providers = Sets.newSet(mock(TraceeBackendProvider.class), mock(TraceeBackendProvider.class));
		final BackendProviderSet out = new BackendProviderSet(providers);
		
		assertThat(out.size(), equalTo(2));
	}

	@Test
	public void shouldReturnMyValues() {
		final TraceeBackendProvider mockedBackend1 = mock(TraceeBackendProvider.class);
		final TraceeBackendProvider mockedBackend2 = mock(TraceeBackendProvider.class);
		final Set<TraceeBackendProvider> providers = Sets.newSet(mockedBackend1, mockedBackend2);
		final BackendProviderSet out = new BackendProviderSet(providers);

		assertThat(out, containsInAnyOrder(mockedBackend1, mockedBackend2));
	}
	
	@Test
	public void shouldHandleEmptySet() {
		final Set<TraceeBackendProvider> providers = Sets.newSet();
		final BackendProviderSet out = new BackendProviderSet(providers);

		assertThat(out.size(), equalTo(0));
	}
	
	@Test
	@Ignore
	public void shouldReturnEmptySetIfOneElementGetPurched() {
		final TraceeBackendProvider mockedBackend1 = mock(TraceeBackendProvider.class);
		final TraceeBackendProvider mockedBackend2 = mock(TraceeBackendProvider.class);
		final Set<TraceeBackendProvider> providers = Sets.newSet(mockedBackend1, mockedBackend2);
		PowerMockito.mock(SoftReference.class);
		final BackendProviderSet out = new BackendProviderSet(providers);

		assertThat(out, containsInAnyOrder(mockedBackend1, mockedBackend2));
	}
}
