package io.tracee.backend.threadlocalstore;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class ThreadLocalTraceeLoggerTest {

	private final ThreadLocalTraceeLogger unit = new ThreadLocalTraceeLogger(this.getClass());

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
		final String logString = unit.buildLogString(ThreadLocalTraceeLogger.LEVEL.WARN, null);
		assertThat(logString, equalTo("WARN - (io.tracee.backend.threadlocalstore.ThreadLocalTraceeLoggerTest) :"));
	}

	@Test
	public void shouldCreateProperStringWithMessage() {
		final String logString = unit.buildLogString(ThreadLocalTraceeLogger.LEVEL.ERROR, "ErrorString");
		assertThat(logString, equalTo("ERROR - (io.tracee.backend.threadlocalstore.ThreadLocalTraceeLoggerTest) :ErrorString"));
	}

	@Test
	public void debugWithoutStacktraceShouldBeDiscarded() throws Exception {
		unit.debug("DebugString");
		verify(System.err, never()).println(anyString());
	}

	@Test
	public void infoWithoutStacktraceShouldBeDiscarded() throws Exception {
		unit.info("InfoString");
		verify(System.err, never()).println(anyString());
	}

	@Test
	public void warnWithoutStacktraceShouldBeDogged() throws Exception {
		unit.warn("WarnString");
		verify(System.err).println(anyString());
	}

	@Test
	public void errorWithoutStacktraceShouldBeDogged() throws Exception {
		unit.error("ErrorString");
		verify(System.err).println(anyString());
	}

	@Test
	public void debugWithStacktraceShouldBeDiscarded() throws Exception {
		unit.debug("DebugString", new Error());
		verify(System.err, never()).println(anyString());
	}

	@Test
	public void infoWithStacktraceShouldBeDiscarded() throws Exception {
		unit.info("InfoString", new Error());
		verify(System.err, never()).println(anyString());
	}

	@Test
	public void warnWithStacktraceShouldBeDogged() throws Exception {
		final Error t = mock(Error.class);
		unit.warn("WarnString", t);
		verify(System.err, atLeastOnce()).println(anyString());
		verify(t).printStackTrace(System.err);
	}

	@Test
	public void errorWithStacktraceShouldBeDogged() throws Exception {
		final Error t = mock(Error.class);
		unit.warn("ErrorString", t);
		verify(System.err, atLeastOnce()).println(anyString());
		verify(t).printStackTrace(System.err);
	}
}