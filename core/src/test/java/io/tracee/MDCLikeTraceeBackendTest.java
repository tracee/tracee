package io.tracee;

import io.tracee.configuration.TraceeFilterConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class MDCLikeTraceeBackendTest {

	@SuppressWarnings("unchecked")
	private final ThreadLocal<Set<String>> traceeKeysMock = (ThreadLocal<Set<String>>) Mockito.mock(ThreadLocal.class);

	private final HashSet<String> traceeKeysSet = Mockito.spy(new HashSet<String>());

	private final TestBackend unit = new TestBackend(traceeKeysMock, null);

	@Before
	public void setUpMocks() {
		when(traceeKeysMock.get()).thenReturn(traceeKeysSet);
	}

	@Test
	public void putWritesEntryToKeysSet() {
		unit.put("Foo", "bar");
		verify(traceeKeysSet).add("Foo");
	}

	@Test
	public void putWritesEntryToMdcLike() {
		unit.put("Foo", "bar");
		assertThat(unit.contextMap, hasEntry("Foo", "bar"));
	}

	@Test
	public void putAllWritesEntriesToKeysSet() {
		final Map<String, String> putMap = new HashMap<String, String>();
		putMap.put("Foo", "bar");
		putMap.put("Ping", "Pong");
		unit.putAll(putMap);
		verify(traceeKeysSet).add("Foo");
		verify(traceeKeysSet).add("Ping");
	}

	@Test
	public void putAllWritesEntriesToMdcLike() {
		final Map<String, String> putMap = new HashMap<String, String>();
		putMap.put("Foo", "bar");
		putMap.put("Ping", "Pong");
		unit.putAll(putMap);
		assertThat(unit.contextMap, hasEntry("Foo", "bar"));
		assertThat(unit.contextMap, hasEntry("Ping", "Pong"));
		assertThat(unit.contextMap.size(), is(2));
	}

	@Test
	public void clearAlsoRemovesTheThreadLocalTraceeKeys() {
		when(traceeKeysSet.iterator()).thenReturn(Collections.<String>emptyList().iterator());
		unit.clear();
		verify(traceeKeysMock).remove();
	}

	@Test
	public void clearRemovesRegisteredKeysFromMdcLike() {
		traceeKeysSet.add("A");
		traceeKeysSet.add("B");
		unit.contextMap.put("A", "a");
		unit.contextMap.put("B", "b");
		unit.clear();
		assertThat(unit.contextMap, not(hasEntry("A", "a")));
		assertThat(unit.contextMap, not(hasEntry("B", "b")));
	}

	@Test
	public void removeRemovesRegisteredKeysFromMDC() {
		when(traceeKeysSet.remove("A")).thenReturn(true);
		when(traceeKeysSet.contains("A")).thenReturn(true);
		unit.contextMap.put("A", "a");
		unit.remove("A");
		assertThat(unit.contextMap.isEmpty(), is(true));
	}

	@Test
	public void removeDoesNotRemoveUnregisteredKeysFromMDC() {
		unit.contextMap.put("A", "a");
		when(traceeKeysSet.remove("A")).thenReturn(false);
		when(traceeKeysSet.contains("A")).thenReturn(false);
		unit.remove("A");
		assertThat(unit.contextMap, hasEntry("A", "a"));
	}

	@Test
	public void sizeCorrespondsToStoredKeysSize() {
		when(traceeKeysSet.size()).thenReturn(42);
		assertThat(unit.size(), equalTo(42));
	}

	@Test
	public void getValueFromMdcIfInKeySet() {
		unit.contextMap.put("A", "hurray");
		when(traceeKeysSet.contains("A")).thenReturn(true);
		assertThat(unit.get("A"), equalTo("hurray"));
	}

	@Test
	public void getDoesNotReadDirectlyFromMdcLike() {
		unit.contextMap.put("A", "hurray");
		when(traceeKeysSet.contains("A")).thenReturn(false);
		assertThat(unit.get("A"), nullValue());
	}

	@Test
	public void containsShouldReturnTrueIfInMDC() {
		unit.contextMap.put("A", "hurray");
		when(traceeKeysSet.contains("A")).thenReturn(true);
		assertThat(unit.containsKey("A"), is(true));
	}

	@Test
	public void containsShouldReturnFalseIfInKeysetButNotMDC() {
		when(traceeKeysSet.contains("A")).thenReturn(true);
		assertThat(unit.containsKey("A"), is(false));
	}

	@Test
	public void isEmptyWhenKeySetIsEmpty() {
		when(traceeKeysSet.isEmpty()).thenReturn(false);
		assertThat(unit.isEmpty(), is(false));
	}

	@Test
	public void copyToMapShouldCreateACopy() {
		unit.contextMap.put("A","foo");
		when(traceeKeysSet.iterator()).thenReturn(Collections.singleton("A").iterator());
		final Map<String, String> copy = unit.copyToMap();
		unit.remove("A");
		assertThat(copy, hasEntry("A","foo"));
	}

	@Test
	public void containsShouldreturnFalseIfNotInMDC() {
		assertThat(unit.containsKey("A"), is(false));
	}

	@Test
	public void testLoadOverwrittenConfigurationValues() {
		assertThat(unit.getConfiguration().generatedRequestIdLength(), equalTo(42));
	}

	@Test
	public void testLoadUserDefinedProfileFromProperties() {
		assertThat(unit.getConfiguration("FOO").shouldProcessParam("ANY", TraceeFilterConfiguration.Channel.IncomingRequest), equalTo(true));
	}

	class TestBackend extends MDCLikeTraceeBackend {

		public Map<String, String> contextMap = new HashMap<String, String>();

		protected TestBackend(ThreadLocal<Set<String>> traceeKeys, TraceeLoggerFactory loggerFactory) {
			super(traceeKeys, loggerFactory);
		}

		@Override
		protected String getFromMdc(String key) {
			return contextMap.get(key);
		}

		@Override
		protected void putToMdc(String key, String value) {
			contextMap.put(key, value);
		}

		@Override
		protected void removeFromMdc(String key) {
			contextMap.remove(key);
		}
	}
}
