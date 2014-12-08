package io.tracee.backend.slf4j;

import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class Slf4jTraceeBackendProviderTest {

	private final Slf4jTraceeBackendProvider unit = new Slf4jTraceeBackendProvider();

	@Test
	public void testProvideBackend() {
		assertThat(unit.provideBackend(), notNullValue());
	}
}
