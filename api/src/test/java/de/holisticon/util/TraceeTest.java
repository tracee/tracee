package de.holisticon.util;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeTest {

	@Test(expected = TraceeException.class)
	public void testGetContext() throws Exception {
		try {
			Tracee.getBackend();
		} catch (TraceeException e) {
			assertThat(e.getMessage(), equalTo("Unable to find a tracee backend provider. Make sure that you have a implementation on your classpath."));
			throw e;
		}
	}
}
