package io.tracee.backend.log4j;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class Log4jTraceeLoggerTest {

	private static final String MESSAGE = "TEST";
	private static final Exception EXCEPTION = new RuntimeException("My exception");

	private Logger mockedLogger = Mockito.mock(Logger.class);

	private Log4jTraceeLogger UNIT = new Log4jTraceeLogger(mockedLogger);


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
		when(mockedLogger.isEnabledFor(eq(Priority.WARN))).thenReturn(true);
		assertThat(UNIT.isWarnEnabled(), is(true));
	}

	@Test
	public void returnTrueIfErrorIsEnabled() {
		when(mockedLogger.isEnabledFor(eq(Priority.ERROR))).thenReturn(true);
		assertThat(UNIT.isErrorEnabled(), is(true));
	}
}
