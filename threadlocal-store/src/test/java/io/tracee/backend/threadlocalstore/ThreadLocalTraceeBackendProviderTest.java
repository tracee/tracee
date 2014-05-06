package io.tracee.backend.threadlocalstore;

import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ThreadLocalTraceeBackendProviderTest {

	private final ThreadLocalTraceeBackendProvider unit = new ThreadLocalTraceeBackendProvider();

	@Test
	public void testProvideBackend() {
		assertThat(unit, notNullValue());
	}

}
