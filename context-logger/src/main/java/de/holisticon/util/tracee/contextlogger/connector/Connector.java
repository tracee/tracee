package de.holisticon.util.tracee.contextlogger.connector;

import java.util.Map;

/**
 *
 * A global interface used to send context information
 *
 * Created by Tobias Gindler, holisticon AG on 14.02.14.
 */
public interface Connector {

    void init(Map<String, String> properties);
    void sendErrorReport(String json);

}
