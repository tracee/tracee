package de.holisticon.util.tracee.contextlogger.connector;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.contextlogger.Connector;

import java.util.Map;

/**
 * Created by Tobias Gindler, holisticon AG on 21.02.14.
 */
public class TraceeConnector implements Connector {

    public static final String MDC_NAME = "context-info-stack";
    public static final String SEPARATOR = "|||---|||";

    private TraceeBackend traceeBackend = Tracee.getBackend();

    @Override
    public void init(Map<String, String> properties) {
        // noting to do
    }

    @Override
    public void sendErrorReport(String json) {

        String existingContent = traceeBackend.get(MDC_NAME);
        traceeBackend.put(MDC_NAME, existingContent != null ? new StringBuilder(existingContent).append(SEPARATOR).append(json).toString() : json);

    }
}
