package io.tracee;

import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Channel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static io.tracee.configuration.TraceeFilterConfiguration.Profile.DISABLED;
import static io.tracee.configuration.TraceeFilterConfiguration.Profile.DISABLE_INBOUND;
import static io.tracee.configuration.TraceeFilterConfiguration.Profile.DISABLE_OUTBOUND;
import static io.tracee.configuration.TraceeFilterConfiguration.Profile.HIDE_INBOUND;
import static io.tracee.configuration.TraceeFilterConfiguration.Profile.HIDE_OUTBOUND;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BackendBaseTest {

	@SuppressWarnings("unchecked")
	private final ThreadLocal<Set<String>> traceeKeysMock = (ThreadLocal<Set<String>>) mock(ThreadLocal.class);

	private final HashSet<String> traceeKeysSet = Mockito.spy(new HashSet<String>());


	private final TestBackend unit = new TestBackend(traceeKeysMock);

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
	public void invocationIdShortcutShouldReturnTheInvocationIdIfSet() {
		unit.put(TraceeConstants.INVOCATION_ID_KEY, "ourInvocationId");
		assertThat(unit.getInvocationId(), is("ourInvocationId"));
	}

	@Test
	public void invocationIdShortcutShouldReturnNullIfNoInvocationIdIsSet() {
		assertThat(unit.getInvocationId(), is(nullValue()));
	}

	@Test
	public void sessionIdShortcutShouldReturnTheSessionIdIfSet() {
		unit.put(TraceeConstants.SESSION_ID_KEY, "ourSessionId");
		assertThat(unit.getSessionId(), is("ourSessionId"));
	}

	@Test
	public void sessionIdShortcutShouldReturnNullIfNoSessionIdIsSet() {
		assertThat(unit.getSessionId(), is(nullValue()));
	}

	@Test
	public void containsShouldreturnFalseIfNotInMDC() {
		assertThat(unit.containsKey("A"), is(false));
	}

	@Test
	public void testLoadOverwrittenConfigurationValues() {
		assertThat(unit.getConfiguration().generatedInvocationIdLength(), equalTo(42));
	}

	@Test
	public void testLoadUserDefinedProfileFromProperties() {
		assertThat(unit.getConfiguration("FOO").shouldProcessParam("ANY", Channel.IncomingRequest), equalTo(true));
	}

	@Test
	public void testHideInboundProfile() {
		assertThat(unit.getConfiguration(HIDE_INBOUND).shouldProcessContext(Channel.OutgoingResponse), equalTo(false));
	}

	@Test
	public void testHideOutboundProfile() {
		assertThat(unit.getConfiguration(HIDE_OUTBOUND).shouldProcessContext(Channel.OutgoingRequest), equalTo(false));
	}

	@Test
	public void testDisableInboundProfile() {
		assertThat(unit.getConfiguration(DISABLE_INBOUND).shouldProcessContext(Channel.IncomingRequest), equalTo(false));
		assertThat(unit.getConfiguration(DISABLE_INBOUND).shouldProcessContext(Channel.OutgoingResponse), equalTo(false));
	}

	@Test
	public void testDisableOutboundProfile() {
		assertThat(unit.getConfiguration(DISABLE_OUTBOUND).shouldProcessContext(Channel.OutgoingRequest), equalTo(false));
		assertThat(unit.getConfiguration(DISABLE_OUTBOUND).shouldProcessContext(Channel.IncomingResponse), equalTo(false));
	}


		@Test
	public void testDisabledProfile() {
		assertThat(unit.getConfiguration(DISABLED).shouldProcessContext(Channel.AsyncDispatch), equalTo(false));
		assertThat(unit.getConfiguration(DISABLED).shouldProcessContext(Channel.AsyncProcess), equalTo(false));
		assertThat(unit.getConfiguration(DISABLED).shouldProcessContext(Channel.IncomingRequest), equalTo(false));
		assertThat(unit.getConfiguration(DISABLED).shouldProcessContext(Channel.IncomingResponse), equalTo(false));
		assertThat(unit.getConfiguration(DISABLED).shouldProcessContext(Channel.OutgoingRequest), equalTo(false));
		assertThat(unit.getConfiguration(DISABLED).shouldProcessContext(Channel.OutgoingResponse), equalTo(false));
	}

	@Test
	public void testNullProfile() {
		assertThat(unit.getConfiguration(null), is(not(nullValue())));
	}

	@Test
	public void testProfileCacheForEmptyOrNullProfile() {
		assertThat(unit.getConfiguration(), is(unit.getConfiguration(null)));
	}

	@Test
	public void testProfileCacheForGivenProfile() {
		final TraceeFilterConfiguration hideInboundConfiguration = unit.getConfiguration(HIDE_INBOUND);
		assertThat(unit.getConfiguration(HIDE_INBOUND), is(sameInstance(hideInboundConfiguration)));
	}

	@Test
	public void testProfileCacheForDifferenceProfiles() {
		final TraceeFilterConfiguration hideInboundConfiguration = unit.getConfiguration(HIDE_OUTBOUND);
		assertThat(unit.getConfiguration(HIDE_INBOUND), is(not(sameInstance(hideInboundConfiguration))));
	}

	class TestBackend extends BackendBase {

		protected final ThreadLocal<Set<String>> traceeKeys;
		public Map<String, String> contextMap = new HashMap<String, String>();

		protected TestBackend(ThreadLocal<Set<String>> traceeKeys) {
			this.traceeKeys = traceeKeys;
		}

		@Override
        public boolean containsKey(String key) {
			return key != null && traceeKeys.get().contains(key) && contextMap.get(key) != null;
        }

		@Override
        public int size() {
            return traceeKeys.get().size();
        }

		@Override
        public boolean isEmpty() {
            return traceeKeys.get().isEmpty();
        }

		@Override
        public String get(String key) {
            if ((key != null) && traceeKeys.get().contains(key))
				return contextMap.get(key);
            else
                return null;
        }

		@Override
        public void put(String key, String value) {
            if (key == null) throw new NullPointerException("null keys are not allowed.");
            if (value == null) throw new NullPointerException("null values are not allowed.");
            final Set<String> registeredKeys = traceeKeys.get();
            if (!registeredKeys.contains(key)) {
                registeredKeys.add(key);
            }
			contextMap.put(key, value);
		}

		@Override
        public void remove(String key) {
            if (key == null) throw new NullPointerException("null keys are not allowed.");
            if (traceeKeys.get().remove(key)) {
				contextMap.remove(key);
			}
        }

		/**
         * Removes all tracee values from the underlying MDC and removes the thread local traceeKeys set.
         */
        @Override
        public void clear() {
            final Set<String> keys = new HashSet<String>(traceeKeys.get());
            for (String key : keys) {
                remove(key);
            }
            traceeKeys.remove();
        }

		@Override
        public void putAll(Map<? extends String, ? extends String> entries) {
            for (Map.Entry<? extends String, ? extends String> entry : entries.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }

		@Override
        public Map<String, String> copyToMap() {
            final Map<String, String> traceeMap = new HashMap<String, String>();
            final Set<String> keys = traceeKeys.get();
            for (String traceeKey : keys) {
				final String value = contextMap.get(traceeKey);
                if (value != null) {
                    traceeMap.put(traceeKey, value);
                }
            }
            return traceeMap;
        }
	}
}
