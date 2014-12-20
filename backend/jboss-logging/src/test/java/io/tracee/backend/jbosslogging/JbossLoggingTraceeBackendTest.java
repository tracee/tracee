package io.tracee.backend.jbosslogging;

import io.tracee.ThreadLocalHashSet;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class JbossLoggingTraceeBackendTest {

	@Test
	public void shouldProvideSlf4JBackend() {
		final JbossLoggingTraceeBackend backend = new JbossLoggingTraceeBackend(new ThreadLocalHashSet<String>());
		assertThat(backend.getLoggerFactory().getLogger(JbossLoggingTraceeBackendTest.class), is(not(nullValue())));
	}
}
