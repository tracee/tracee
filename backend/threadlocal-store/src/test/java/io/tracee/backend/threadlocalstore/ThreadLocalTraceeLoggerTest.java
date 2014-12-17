package io.tracee.backend.threadlocalstore;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class ThreadLocalTraceeLoggerTest {

	private final ThreadLocalTraceeLogger UNIT = new ThreadLocalTraceeLogger(this.getClass());

	@Before
	public void setupMocks() throws Exception {
		// Replacing System.err with a mocked PrintStream
		final Field errField = Whitebox.getField(System.class, "err");
		errField.setAccessible(true);
		// Remove final modifier of System.err
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(errField, errField.getModifiers() & ~Modifier.FINAL);
		// Replace with mock
		errField.set(System.class, mock(PrintStream.class));
	}

	@Test
	public void shouldCreateProperStringWithoutMessage() {
		final String logString = UNIT.buildLogString(ThreadLocalTraceeLogger.LEVEL.WARN, null);
		assertThat(logString, equalTo("WARN - (io.tracee.backend.threadlocalstore.ThreadLocalTraceeLoggerTest) :"));
	}

	@Test
	public void shouldCreateProperStringWithMessage() {
		final String logString = UNIT.buildLogString(ThreadLocalTraceeLogger.LEVEL.ERROR, "ErrorString");
		assertThat(logString, equalTo("ERROR - (io.tracee.backend.threadlocalstore.ThreadLocalTraceeLoggerTest) :ErrorString"));
	}

	@Test
	public void debugWithoutStacktraceShouldBeDiscarded() throws Exception {
		UNIT.debug("DebugString");
		verify(System.err, never()).println(anyString());
	}

	@Test
	public void infoWithoutStacktraceShouldBeDiscarded() throws Exception {
		UNIT.info("InfoString");
		verify(System.err, never()).println(anyString());
	}

	@Test
	public void warnWithoutStacktraceShouldBeLogged() throws Exception {
		UNIT.warn("WarnString");
		verify(System.err).println(anyString());
	}

	@Test
	public void errorWithoutStacktraceShouldBeLogged() throws Exception {
		UNIT.error("ErrorString");
		verify(System.err).println(anyString());
	}

	@Test
	public void debugWithStacktraceShouldBeDiscarded() throws Exception {
		final Error t = mock(Error.class);
		UNIT.debug("DebugString", t);
		verify(System.err, never()).println(anyString());
		verify(t, never()).printStackTrace(System.err);
	}

	@Test
	public void infoWithStacktraceShouldBeDiscarded() throws Exception {
		final Error t = mock(Error.class);
		UNIT.info("InfoString", t);
		verify(System.err, never()).println(anyString());
		verify(t, never()).printStackTrace(System.err);
	}

	@Test
	public void warnWithStacktraceShouldBeLogged() throws Exception {
		final Error t = mock(Error.class);
		UNIT.warn("WarnString", t);
		verify(System.err, atLeastOnce()).println(anyString());
		verify(t).printStackTrace(System.err);
	}

	@Test
	public void errorWithStacktraceShouldBeLogged() throws Exception {
		final Error t = mock(Error.class);
		UNIT.error("ErrorString", t);
		verify(System.err, atLeastOnce()).println(anyString());
		verify(t).printStackTrace(System.err);
	}

	@Test
	public void returnFalseBecauseDebugLoggingIsAlwaysDisabled() {
		MatcherAssert.assertThat(UNIT.isDebugEnabled(), is(false));
	}

	@Test
	public void returnFalseBecauseInfoLoggingIsAlwaysDisabled() {
		MatcherAssert.assertThat(UNIT.isInfoEnabled(), is(false));
	}

	@Test
	public void returnTrueBecauseWarnLoggingIsAlwaysEnabled() {
		MatcherAssert.assertThat(UNIT.isWarnEnabled(), is(true));
	}

	@Test
	public void returnTrueBecauseErrorLoggingIsAlwaysEnabled() {
		MatcherAssert.assertThat(UNIT.isErrorEnabled(), is(true));
	}
}
