package de.holisticon.util.tracee.backend.threadlocalstore;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class ThreadLocalTraceeBackendTest {

    private ThreadLocalTraceeBackend unit = new ThreadLocalTraceeBackend(new ThreadLocalMap());

    @Test
    public void testStoredKeys() {
        assertThat(unit.getRegisteredKeys().size(), equalTo(0));
        unit.put("FOO", "BAR");
        assertThat(unit.get("FOO"), equalTo("BAR"));
        assertThat(unit.getRegisteredKeys().size(), equalTo(1));
    }

    @Test
    public void testExtractContext() {
        unit.put("FUBI", "BARBI");
        assertThat(unit.getRegisteredKeys(), Matchers.contains("FUBI"));
        assertThat(unit.extractContext(), hasEntry("FUBI", "BARBI"));
    }

}
