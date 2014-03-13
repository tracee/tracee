package de.holisticon.util.tracee.backend.log4j2;

import org.apache.logging.log4j.Logger;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class Log4j2TraceeLoggerTest {

	private static final Object MESSAGE = "TEST";
	private static final Exception EXCEPTION = new RuntimeException("My exception");


	private final Logger mockedLogger = mock(Logger.class);
	private final Log4J2TraceeLogger UNIT = new Log4J2TraceeLogger(mockedLogger);

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

}
