package io.tracee.backend.slf4j;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

public class Slf4jTraceeBackendIT {

	@Deprecated
	@Test
	public void traceeShouldReturnSlf4JBackendByDefault() {
		assertThat(Tracee.getBackend(), is(instanceOf(TraceeBackend.class)));
	}

	@Deprecated
	@Test
	public void shouldReturnAlwaystheSameDefaultBackend() {
		final TraceeBackend backend = Tracee.getBackend();
		assertThat(Tracee.getBackend(), is(sameInstance(backend)));
	}
}
