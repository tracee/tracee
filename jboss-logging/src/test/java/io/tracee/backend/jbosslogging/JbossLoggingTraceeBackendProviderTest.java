package io.tracee.backend.jbosslogging;

import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class JbossLoggingTraceeBackendProviderTest {

	private final JbossLoggingTraceeBackendProvider unit = new JbossLoggingTraceeBackendProvider();

	@Test
	public void testProvideBackend() {
		assertThat(unit.provideBackend(), notNullValue());
	}
}
