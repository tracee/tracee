package de.holisticon.util.tracee;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class MDCLikeTraceeBackendTest {

	private final MDCLike mdcLikeMock = Mockito.mock(MDCLike.class);
	@SuppressWarnings("unchecked")
	private final ThreadLocal<Set<String>> traceeKeysMock = (ThreadLocal<Set<String>>) Mockito.mock(ThreadLocal.class);
	@SuppressWarnings("unchecked")
	private final Set<String> traceeKeysSet = (Set<String>) Mockito.mock(Set.class);

	private final MDCLikeTraceeBackend unit = new MDCLikeTraceeBackend(mdcLikeMock, traceeKeysMock) {
		@Override
		public TraceeLoggerFactory getLoggerFactory() {
			return null;
		}
	};

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
		verify(mdcLikeMock).put("Foo", "bar");
	}

	@Test
	public void clearAlsoRemovesTheThreadLocalTraceeKeys() {
		when(traceeKeysSet.iterator()).thenReturn(Collections.<String>emptyIterator());
		unit.clear();
		verify(traceeKeysMock).remove();
	}

	@Test
	public void clearRemovesRegisteredKeysFromMdcLike() {
		when(traceeKeysSet.iterator()).thenReturn(Arrays.asList("A", "B").iterator());
		unit.clear();
		verify(mdcLikeMock).remove("A");
		verify(mdcLikeMock).remove("B");
	}

	@Test
	public void removeRemovesRegisteredKeysFromMDC() {
		when(traceeKeysSet.remove("A")).thenReturn(true);
		when(traceeKeysSet.contains("A")).thenReturn(true);
		unit.remove("A");
		verify(mdcLikeMock).remove("A");
	}

	@Test
	public void removeDoesNotRemoveUnregisteredKeysFromMDC() {
		when(traceeKeysSet.remove("A")).thenReturn(false);
		when(traceeKeysSet.contains("A")).thenReturn(false);
		unit.remove("A");
		verify(mdcLikeMock, never()).remove("A");
	}

	@Test
	public void sizeCorrespondsToStoredKeysSize() {
		when(traceeKeysSet.size()).thenReturn(42);
		assertThat(unit.size(), equalTo(42));
	}

	@Test
	public void getValueFromMdcIfInKeySet() {
		when(traceeKeysSet.contains("A")).thenReturn(true);
		when(mdcLikeMock.get("A")).thenReturn("hurray");
		assertThat(unit.get("A"), equalTo("hurray"));
	}

	@Test
	public void getDoesNotReadDirectlyFromMdcLike() {
		when(traceeKeysSet.contains("A")).thenReturn(false);
		when(mdcLikeMock.get("A")).thenReturn("hurray");
		assertThat(unit.get("A"), nullValue());
	}

}
