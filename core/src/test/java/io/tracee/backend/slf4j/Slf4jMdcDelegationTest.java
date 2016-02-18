package io.tracee.backend.slf4j;

import io.tracee.ThreadLocalHashSet;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.MDC;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MDC.class)
public class Slf4jMdcDelegationTest {

	private final ThreadLocalHashSet<String> traceeKeys = new ThreadLocalHashSet<>();

	private final Slf4jTraceeBackend unit = new Slf4jTraceeBackend(traceeKeys);

	@Before
	public void setup() {
		PowerMockito.mockStatic(MDC.class);
		traceeKeys.get().clear();
	}

	@Test
	public void shouldPutToMDC() {
		unit.put("A", "vA");
		PowerMockito.verifyStatic();
		MDC.put("A", "vA");
	}

	@Test
	public void putWritesEntryToKeysSet() {
		unit.put("Foo", "bar");
		assertThat(traceeKeys.get(), contains("Foo"));
	}

	@Test
	public void putAllWritesEntriesToMdcLike() {
		final Map<String, String> putMap = new HashMap<>();
		putMap.put("Foo", "bar");
		putMap.put("Ping", "Pong");
		unit.putAll(putMap);
		PowerMockito.verifyStatic(times(1));
		MDC.put(eq("Foo"), eq("bar"));
		PowerMockito.verifyStatic(times(1));
		MDC.put(eq("Ping"), eq("Pong"));
		PowerMockito.verifyNoMoreInteractions(MDC.class);
	}

	@Test
	public void putAllWritesEntriesToKeysSet() {
		final Map<String, String> putMap = new HashMap<>();
		putMap.put("Foo", "bar");
		putMap.put("Ping", "Pong");
		unit.putAll(putMap);
		assertThat(traceeKeys.get(), contains("Foo", "Ping"));
	}

	@Test
	public void clearAlsoRemovesTheThreadLocalTraceeKeys() {
		traceeKeys.get().add("test");
		unit.clear();
		verifyStatic(times(1));
		MDC.remove("test");
		PowerMockito.verifyNoMoreInteractions(MDC.class);
	}

	@Test
	public void containsShouldReturnTrueIfInMdcAndKeySet() {
		traceeKeys.get().add("BB");
		when(MDC.get("BB")).thenReturn("vBB");
		assertThat(unit.containsKey("BB"), is(true));
	}

	@Test
	public void containsShouldReturnFalseIfInKeysetButNotMDC() {
		traceeKeys.get().add("BB");
		assertThat(unit.containsKey("BB"), is(false));
	}

	@Test
	public void containsShouldReturnFalseIfInMdcButNotInKeyset() {
		when(MDC.get("BB")).thenReturn("vBB");
		assertThat(unit.containsKey("BB"), is(false));
	}

	@Test
	public void getValueFromMdcIfInKeySet() {
		traceeKeys.get().add("A");
		PowerMockito.when(MDC.get(eq("A"))).thenReturn("hurray");
		assertThat(unit.get("A"), equalTo("hurray"));
	}

	@Test
	public void getDoesNotReadFromMdcWhenKeyIsNotInKeySet() {
		PowerMockito.when(MDC.get(eq("A"))).thenReturn("hurray");
		assertThat(unit.get("A"), nullValue());
	}

	@Test
	public void callOnRemoveShouldRemoveValueFromMDC() {
		traceeKeys.get().add("BB");
		unit.remove("BB");
		PowerMockito.verifyStatic();
		MDC.remove("BB");
	}

	@Test
	public void callOnRemoveShouldRemoveValueFromTraceeKeys() {
		traceeKeys.get().add("BB");
		unit.remove("BB");
		assertThat(traceeKeys.get(), empty());
	}

	@Test
	public void removeDoesNotRemoveUnregisteredKeysFromMDC() {
		unit.remove("A");
		PowerMockito.verifyNoMoreInteractions(MDC.class);
	}

	@Test
	public void sizeCorrespondsToStoredKeysSize() {
		traceeKeys.get().addAll(Arrays.asList("A", "B", "C", "D"));
		assertThat(unit.size(), equalTo(4));
	}

	@Test
	public void isEmptyWhenKeySetIsEmptyRegardlessOfMDC() {
		PowerMockito.when(MDC.get(eq("A"))).thenReturn("hurray");
		assertThat(unit.isEmpty(), Matchers.is(true));
	}

	@Test
	public void isNotEmptyIfKeySetContainsValuesRegardlessOfMDC() {
		traceeKeys.get().add("A");
		Assert.assertThat(unit.isEmpty(), is(false));
	}

	@Test
	public void copyToMapShouldCreateACopy() {
		PowerMockito.when(MDC.get(eq("A"))).thenReturn("AAb");
		traceeKeys.get().add("A");

		final Map<String, String> copyOfMdc = unit.copyToMap();
		assertThat(copyOfMdc, hasEntry("A", "AAb"));
		assertThat(copyOfMdc.size(), is(1));
	}
}
