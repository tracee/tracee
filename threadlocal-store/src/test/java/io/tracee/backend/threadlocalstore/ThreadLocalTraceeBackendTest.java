package io.tracee.backend.threadlocalstore;

import io.tracee.ThreadLocalHashSet;
import io.tracee.TraceeLogger;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

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

	@Test
	public void shouldReturnTraceeBackendWithThreadLocalLogger() {
		TraceeLogger traceeLogger = unit.getLoggerFactory().getLogger(this.getClass());
		assertThat(traceeLogger, is(instanceOf(ThreadLocalTraceeLogger.class)));
	}

}
