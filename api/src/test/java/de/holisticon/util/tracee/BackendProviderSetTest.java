package de.holisticon.util.tracee;

import de.holisticon.util.tracee.spi.TraceeBackendProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.Sets;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author Sven Bunge, Holisticon AG
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(BackendProviderSet.class)
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
	public void shouldReturnEmptySetIfOneElementWasDetached() throws Exception {
		final TraceeBackendProvider mockedBackend1 = mock(TraceeBackendProvider.class);
		final TraceeBackendProvider mockedBackend2 = mock(TraceeBackendProvider.class);
		
		// Simulate a SoftReference with an empty value. (The powermock-method withArgument(..) don't work here!)
		final SoftReference backendRef1 = new SoftReference<TraceeBackendProvider>(mockedBackend1);
		final SoftReference backendRef2 = new SoftReference<TraceeBackendProvider>(null);
		whenNew(SoftReference.class).withAnyArguments().thenReturn(backendRef1, backendRef2);

		// Setup Set and ask for size - then we extract the 'valid' field to check that the set is marked as invalid
		final BackendProviderSet traceeBackendProviders = new BackendProviderSet(Sets.newSet(mockedBackend1, mockedBackend2));
		final Field validField = Whitebox.getField(BackendProviderSet.class, "valid");
		assertThat(traceeBackendProviders.size(), equalTo(0));
		assertThat(validField.getBoolean(traceeBackendProviders), is(false));
	}
}
