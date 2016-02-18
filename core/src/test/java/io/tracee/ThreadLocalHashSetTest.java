package io.tracee;

import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ThreadLocalHashSetTest {

	private final ThreadLocalHashSet<String> unit = new ThreadLocalHashSet<>();

	@Test
	public void testGetInitialValue() {
		assertThat(unit.get(), notNullValue());
	}

}
