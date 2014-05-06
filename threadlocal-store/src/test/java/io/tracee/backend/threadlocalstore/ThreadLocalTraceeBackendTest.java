package io.tracee.backend.threadlocalstore;

import io.tracee.ThreadLocalHashSet;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasEntry;

public class ThreadLocalTraceeBackendTest {

    private ThreadLocalMap<String, String> backedMap = new ThreadLocalMap<String,String>();
    private MdcLikeThreadLocalMapAdapter adapter = new MdcLikeThreadLocalMapAdapter(backedMap);
    private ThreadLocalTraceeBackend unit = new ThreadLocalTraceeBackend(adapter, new ThreadLocalHashSet<String>());

    @Test
    public void testStoredKeys() {
        unit.clear();
        assertThat(unit.keySet().size(), equalTo(0));
        unit.put("FOO", "BAR");
        assertThat(unit.get("FOO"), equalTo("BAR"));
        assertThat(unit.keySet().size(), equalTo(1));
    }

    @Test
    public void testExtractContext() {
        unit.put("FUBI", "BARBI");
        assertThat(unit.keySet(), contains("FUBI"));
        assertThat(unit, hasEntry("FUBI", "BARBI"));
    }

}
