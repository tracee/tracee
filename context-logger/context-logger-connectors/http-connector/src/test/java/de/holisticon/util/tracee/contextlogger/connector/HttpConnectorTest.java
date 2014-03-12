package de.holisticon.util.tracee.contextlogger.connector;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tobias Gindler, holisticon AG on 20.02.14.
 */
public class HttpConnectorTest {

    //@Ignore
    @Test
    public void testHttpConnector() {

        final Map<String, String> map = new HashMap<String, String>();
        map.put(HttpConnector.PROPERTY_URL, "https://www.holisticon.de");

        final HttpConnector hc = new HttpConnector();
        hc.init(map);

        hc.sendErrorReport("{'abc':'def'}");

        try {
            Thread.sleep(1000);
        }
        catch (final InterruptedException e) {
            e.printStackTrace();
        }

    }

}
