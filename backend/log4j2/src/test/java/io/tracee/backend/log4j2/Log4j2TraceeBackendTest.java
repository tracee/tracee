package io.tracee.backend.log4j2;

import io.tracee.ThreadLocalHashSet;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class Log4j2TraceeBackendTest {

	@Test
	public void shouldProvideSlf4JBackend() {
		final Log4j2TraceeBackend backend = new Log4j2TraceeBackend(new ThreadLocalHashSet<String>());
		assertThat(backend.getLoggerFactory().getLogger(Log4j2TraceeBackendTest.class), is(not(nullValue())));
	}
}
