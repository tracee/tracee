package io.tracee.backend.slf4j;

import io.tracee.ThreadLocalHashSet;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class Slf4jTraceeBackendTest {

	@Test
	public void shouldProvideSlf4JBackend() {
		final Slf4jTraceeBackend backend = new Slf4jTraceeBackend(new Slf4jMdcAdapter(), new ThreadLocalHashSet<String>());
		assertThat(backend.getLoggerFactory().getLogger(Slf4jTraceeBackendTest.class), is(not(nullValue())));
	}
}
