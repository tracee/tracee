package io.tracee.backend.threadlocalstore;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ThreadLocalContextProviderIT {

	@Deprecated
    @Test
    public void shouldDetectThreadLocalStoreBySPI() {
        final TraceeBackend context = Tracee.getBackend();
        assertThat(context, Matchers.instanceOf(ThreadLocalTraceeBackend.class));
    }

	@Deprecated
    @Test
    public void testLoadProvierThenStoreAndRetrieve() {
        final TraceeBackend context = Tracee.getBackend();
        context.put("FOO", "BAR");
        assertThat(context.get("FOO"), equalTo("BAR"));
        context.clear();
    }

	@Test
	public void testStoreAndRetrieve() {
		final TraceeBackend context = new ThreadLocalTraceeBackendProvider().provideBackend();
		context.put("FOO", "BAR");
		assertThat(context.get("FOO"), equalTo("BAR"));
		context.clear();
	}
}
