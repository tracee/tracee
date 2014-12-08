package io.tracee.backend.log4j;

import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class Log4jTraceeBackendProviderTest {

	private final Log4jTraceeBackendProvider unit = new Log4jTraceeBackendProvider();

	@Test
	public void testProvideBackend() {
		assertThat(unit.provideBackend(), notNullValue());
	}

}
