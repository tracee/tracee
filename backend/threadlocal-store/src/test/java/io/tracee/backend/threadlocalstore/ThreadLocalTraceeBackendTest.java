package io.tracee.backend.threadlocalstore;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
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

	@Test
	public void ifTheSizeIsZeroTheBackendShouldBeEmpty() {
		assertThat(unit.size(), is(0));
		assertThat(unit.isEmpty(), is(true));
	}

	@Test
	public void ifTheSizeIsNotZeroTheBackendShouldNotBeEmpty() {
		unit.put("test", "testVal");
		assertThat(unit.size(), is(1));
		assertThat(unit.isEmpty(), is(false));
	}

	@Test
	public void copyToMapShouldCopyContentToNewMap() {
		unit.put("test1", "testVal1");
		unit.put("test2", "testVal2");
		final Map<String, String> mapCopy = unit.copyToMap();
		assertThat(mapCopy, hasEntry("test1", "testVal1"));
		assertThat(mapCopy, hasEntry("test2", "testVal2"));
		assertThat(mapCopy.size(), is(2));
	}

	@Test
	public void copiedMapsDoNotEffektEachOther() {
		unit.put("test1", "testVal1");
		final Map<String, String> mapCopy = unit.copyToMap();
		assertThat(mapCopy, hasEntry("test1", "testVal1"));
		unit.put("newKey2", "newVal2");
		assertThat(mapCopy.size(), is(1));
		assertThat(unit.size(), is(2));

		mapCopy.put("anotherKey", "anotherValue");
		assertThat(unit.containsKey("anotherKey"), is(false));
	}

	@Test
	public void putAllMethodAddsAllElementsOfGivenMap() {
		final Map<String, String> givenMap = new HashMap<>();
		givenMap.put("key1", "value1");
		givenMap.put("key2", "value2");
		unit.putAll(givenMap);
		assertThat(unit.size(), is(2));
		assertThat(unit.containsKey("key1"), is(true));
		assertThat(unit.containsKey("key2"), is(true));
	}


}
