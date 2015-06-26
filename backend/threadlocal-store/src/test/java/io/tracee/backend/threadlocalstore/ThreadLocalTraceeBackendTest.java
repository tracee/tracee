package io.tracee.backend.threadlocalstore;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ThreadLocalTraceeBackendTest {

    private ThreadLocalTraceeBackend unit = new ThreadLocalTraceeBackend();

    @Test
    public void testStoredKeys() {
        unit.clear();
        assertThat(unit.size(), equalTo(0));
        unit.put("FOO", "BAR");
        assertThat(unit.get("FOO"), equalTo("BAR"));
        assertThat(unit.size(), equalTo(1));
    }

    @Test
    public void testExtractContext() {
        unit.put("FUBI", "BARBI");
        assertThat(unit.get("FUBI"), is("BARBI"));
    }

}
