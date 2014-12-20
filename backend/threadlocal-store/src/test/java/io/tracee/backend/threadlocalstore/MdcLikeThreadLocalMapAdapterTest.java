package io.tracee.backend.threadlocalstore;

import io.tracee.ThreadLocalHashSet;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MdcLikeThreadLocalMapAdapterTest {
	private final ThreadLocalHashSet<String> traceeKeys = new ThreadLocalHashSet<String>();

	private final ThreadLocalTraceeBackend unit = new ThreadLocalTraceeBackend(traceeKeys);

	@Before
	public void setup() {
		traceeKeys.get().add("BB");
	}

	@Test
	public void shouldPutToMDC() {
		unit.put("A", "vA");
		assertThat(unit.getThreadLocalMap().get(), hasEntry("A", "vA"));
	}

	@Test
	public void shouldReturnTrueIfKeyIsInMDC() {
		unit.getThreadLocalMap().get().put("BB", "vBB");
		assertThat(unit.containsKey("BB"), is(true));
	}

	@Test
	public void shouldReturnValueFromMDC() {
		unit.getThreadLocalMap().get().put("BB", "vBB");
		assertThat(unit.get("BB"), is("vBB"));
	}

	@Test
	public void shouldCallRemoveOnMDC() {
		unit.getThreadLocalMap().get().put("BB", "vBB");
		unit.remove("BB");
		assertThat(unit.getThreadLocalMap().get().containsKey("BB"), is(false));
	}
}
