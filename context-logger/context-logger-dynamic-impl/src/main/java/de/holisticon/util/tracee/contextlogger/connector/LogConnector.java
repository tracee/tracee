package de.holisticon.util.tracee.contextlogger.connector;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeLogger;
import de.holisticon.util.tracee.contextlogger.Connector;

import java.util.Map;

/**
 * A Connector to send error reports to the logger.
 * Created by Tobias Gindler, holisticon AG on 07.02.14.
 */
public class LogConnector implements Connector {

    private TraceeLogger logger = Tracee.getBackend().getLoggerFactory().getLogger(LogConnector.class);

    @Override
    public void init(Map<String, String> properties) {

    }

    @Override
    public void sendErrorReport(String json) {
        logger.error(json);
    }
}
