package de.holisticon.util.tracee;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Daniel
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
        assertThat(expected.getMessage(), equalTo("Unable to find a context provider"));

    }
}
