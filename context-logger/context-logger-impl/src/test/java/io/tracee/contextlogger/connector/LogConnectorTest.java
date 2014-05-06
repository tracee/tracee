package io.tracee.contextlogger.connector;

import io.tracee.TraceeLogger;
import org.junit.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LogConnectorTest {

	private final TraceeLogger logger = mock(TraceeLogger.class);
	private final LogConnector unit = new LogConnector(logger);

	@Test
	public void testLog() {
		unit.sendErrorReport("{ \"foo\":\"bar\"}");
		verify(logger).error(eq("{ \"foo\":\"bar\"}"));
	}

}
