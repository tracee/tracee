package io.tracee.backend.slf4j;

import io.tracee.ThreadLocalHashSet;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class Slf4jTraceeBackendTest {

	private final ThreadLocalHashSet<String> traceeKeys = new ThreadLocalHashSet<>();

	private final Slf4jTraceeBackend OUT = new Slf4jTraceeBackend(traceeKeys);

	@Before
	public void before() {
		traceeKeys.get().clear();
	}

	@Test
	public void keyShouldExistOnlyIfInTraceeKeys() {
		MDC.put("test", "test");
		traceeKeys.get().add("test");
		assertThat(OUT.containsKey("test"), is(true));
	}

	@Test
	public void sizeShouldRespondWithTraceeKeysSize() {
		traceeKeys.get().add("a");
		traceeKeys.get().add("b");
		assertThat(OUT.size(), is(2));
	}

	@Test
	public void backendIsEmptyIfTraceeKeysIsEmpty() {
		MDC.put("test", "test");
		assertThat(OUT.isEmpty(), is(true));
	}

	@Test
	public void backendIsNotEmptryIfTraceeKeysIsNotEmpty() {
		traceeKeys.get().add("a");
		assertThat(OUT.isEmpty(), is(false));
	}

	@Test
	public void clearRemovesAllKeysInTraceeKeysFromMdc() {
		traceeKeys.get().add("a");
		MDC.put("a", "a");
		MDC.put("b", "b");
		OUT.clear();
		assertThat(MDC.get("a"), is(nullValue()));
		assertThat(MDC.get("b"), is("b"));
	}

	@Test
	public void putAddsToMdcAndTraceeKeys() {
		OUT.put("a", "ab");
		assertThat(traceeKeys.get().iterator().next(), is("a"));
		assertThat(MDC.get("a"), is("ab"));
	}

	@Test
	public void putAllAddsAllTupleToMdcAndTraceeKeys() {
		final Map<String, String> testMap = new HashMap<>();
		testMap.put("a", "ab");
		testMap.put("b", "bc");
		OUT.putAll(testMap);
		assertThat(traceeKeys.get().size(), is(2));
		assertThat(MDC.get("a"), is("ab"));
		assertThat(MDC.get("b"), is("bc"));
	}

	@Test
	public void copyToMapShouldCopyAllTraceeKeys() {
		MDC.put("a", "ab");
		MDC.put("b", "bc");
		traceeKeys.get().add("b");
		final Map<String, String> traceeCopy = OUT.copyToMap();
		assertThat(traceeCopy.size(), is(1));
		assertThat(traceeCopy.get("b"), is("bc"));
	}

	@Test
	public void returnNullIfKeyIsNull() {
		assertThat(OUT.get(null), is(nullValue()));
	}

	@Test
	public void returnNullIfKeyIsNotInTraceeValues() {
		MDC.put("a", "a");
		assertThat(OUT.get("a"), is(nullValue()));
	}

}
