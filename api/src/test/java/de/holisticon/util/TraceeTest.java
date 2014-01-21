package de.holisticon.util;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeTest {

    @Test
    public void testGetContext() throws Exception {
        TraceeException expected = null;
        try {
            Tracee.getBackend();
        } catch (TraceeException e) {
            expected = e;
        }
        assertThat(expected, notNullValue());
        assertThat(expected.getMessage(), equalTo("Unable to find a tracee backend provider"));

    }
}
