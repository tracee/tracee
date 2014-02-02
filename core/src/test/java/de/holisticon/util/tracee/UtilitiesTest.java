package de.holisticon.util.tracee;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class UtilitiesTest {

    @Test
    public void testCreateRandomAlphanumeric() {
        final String randomAlphanumeric = Utilities.createRandomAlphanumeric(32);
        assertThat("length", randomAlphanumeric.length(), equalTo(32));
    }

}
