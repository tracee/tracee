package io.tracee.backend.jbosslogging;

import org.jboss.logging.Logger;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JbossLoggingTraceeLoggerTest {

	private static final String MESSAGE = "TEST";
	private static final Exception EXCEPTION = new RuntimeException("My exception");

	private Logger mockedLogger = mock(Logger.class);
	private JbossLoggingTraceeLogger UNIT = new JbossLoggingTraceeLogger(mockedLogger);

	@Test
	public void logDebugMessageWithLogger() {
		UNIT.debug(MESSAGE);
		verify(mockedLogger).debug(MESSAGE);
	}

	@Test
	public void logDebugMessageAndExceptionWithLogger() {
		UNIT.debug(MESSAGE, EXCEPTION);
		verify(mockedLogger).debug(MESSAGE, EXCEPTION);
	}

	@Test
	public void logInfoMessageWithLogger() {
		UNIT.info(MESSAGE);
		verify(mockedLogger).info(MESSAGE);
	}

	@Test
	public void logInfoMessageAndExceptionWithLogger() {
		UNIT.info(MESSAGE, EXCEPTION);
		verify(mockedLogger).info(MESSAGE, EXCEPTION);
	}

	@Test
	public void logWarnMessageWithLogger() {
		UNIT.warn(MESSAGE);
		verify(mockedLogger).warn(MESSAGE);
	}

	@Test
	public void logWarnMessageAndExceptionWithLogger() {
		UNIT.warn(MESSAGE, EXCEPTION);
		verify(mockedLogger).warn(MESSAGE, EXCEPTION);
	}

	@Test
	public void logErrorMessageWithLogger() {
		UNIT.error(MESSAGE);
		verify(mockedLogger).error(MESSAGE);
	}

	@Test
	public void logErrorMessageAndExceptionWithLogger() {
		UNIT.error(MESSAGE, EXCEPTION);
		verify(mockedLogger).error(MESSAGE, EXCEPTION);
	}

	@Test
	public void returnTrueIfDebugIsEnabled() {
		when(mockedLogger.isDebugEnabled()).thenReturn(true);
		assertThat(UNIT.isDebugEnabled(), is(true));
	}

	@Test
	public void returnTrueIfInfoIsEnabled() {
		when(mockedLogger.isInfoEnabled()).thenReturn(true);
		assertThat(UNIT.isInfoEnabled(), is(true));
	}

	@Test
	public void returnTrueIfWarnIsEnabled() {
		when(mockedLogger.isEnabled(eq(Logger.Level.WARN))).thenReturn(true);
		assertThat(UNIT.isWarnEnabled(), is(true));
	}

	@Test
	public void returnTrueIfErrorIsEnabled() {
		when(mockedLogger.isEnabled(eq(Logger.Level.ERROR))).thenReturn(true);
		assertThat(UNIT.isErrorEnabled(), is(true));
	}
}
