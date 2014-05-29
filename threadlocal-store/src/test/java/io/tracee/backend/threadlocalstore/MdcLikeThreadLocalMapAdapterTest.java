package io.tracee.backend.threadlocalstore;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MdcLikeThreadLocalMapAdapterTest {

	private final ThreadLocalMap<String, String> map = new ThreadLocalMap<String, String>();

	private final MdcLikeThreadLocalMapAdapter unit = new MdcLikeThreadLocalMapAdapter(map);

	@Test
	public void shouldPutToMDC() {
		unit.put("A", "vA");
		assertThat(map.get().keySet(), containsInAnyOrder("A"));
		assertThat(map.get().values(), containsInAnyOrder("vA"));
	}

	@Test
	public void shouldReturnTrueIfValueIsInMDC() {
		map.get().put("A", "vA");
		assertThat(unit.containsKey("A"), is(true));
	}

	@Test
	public void shouldReturnValueFromMDC() {
		map.get().put("A", "vA");
		assertThat(unit.get("A"), is("vA"));
	}

	@Test
	public void shouldCallRemoveOnMDC() {
		map.get().put("A", "vA");
		unit.remove("A");
		assertThat(map.get().containsKey("A"), is(false));
	}

	@Test
	public void shouldCallCopyMethodOnSlf4JMDC() {
		map.get().put("A", "vA");
		map.get().put("B", "vB");
		assertThat(unit.getCopyOfContext(), equalTo(map.get()));
		assertThat(unit.getCopyOfContext(), is(not(sameInstance(map.get()))));
	}

}