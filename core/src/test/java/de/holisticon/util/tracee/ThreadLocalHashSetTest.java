package de.holisticon.util.tracee;

import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class ThreadLocalHashSetTest {

	private final ThreadLocalHashSet<String> unit = new ThreadLocalHashSet<String>();

	@Test
	public void testGetInitialValue() {
		assertThat(unit.get(), notNullValue());
	}

}
