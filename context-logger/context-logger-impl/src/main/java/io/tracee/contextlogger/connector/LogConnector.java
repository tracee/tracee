package io.tracee.contextlogger.connector;

import io.tracee.Tracee;
import io.tracee.TraceeLogger;
import io.tracee.contextlogger.Connector;

import java.util.Map;

/**
 * A Connector to send error reports to the logger.
 * Created by Tobias Gindler, holisticon AG on 07.02.14.
 */
public class LogConnector implements Connector {


	public LogConnector() {
		this(Tracee.getBackend().getLoggerFactory().getLogger(LogConnector.class));
	}

	LogConnector(TraceeLogger logger) {
		this.logger = logger;
	}

    private final TraceeLogger logger;

    @Override
    public void init(Map<String, String> properties) {

    }

    @Override
    public final void sendErrorReport(String json) {
        logger.error(json);
    }
}
