package io.tracee.backend.threadlocalstore;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class ThreadLocalMapTest {

	@Test
	public void initialValueShouldCreateEmptyMap() {
		Map<String, String> map = new ThreadLocalMap<String, String>().initialValue();
		assertThat(map, is(instanceOf(Map.class)));
		assertThat(map.size(), is(0));
	}

	@Test
	public void shouldCreateChildWithNull() {
		assertThat(new ThreadLocalMap<String, String>().childValue(null), is(nullValue()));
	}

	@Test
	public void shouldCreateChildWithCopyOfHashMap() {
		final Map<String, String> map = new HashMap<>();
		map.put("A", "vA");
		final Map<String, String> childValueMap = new ThreadLocalMap<String, String>().childValue(map);
		assertThat(childValueMap, equalTo(map));
		assertThat(childValueMap, is(not(sameInstance(map))));
	}
}
