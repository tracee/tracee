package de.holisticon.util.tracee.contextlogger.connector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

import de.holisticon.util.tracee.contextlogger.TraceeContextLoggerConstants;

/**
 * Test for ConnectorFactory.
 * Created by Tobias Gindler, holisticon AG on 14.02.14.
 */
public class ConnectorFactoryTest {

    /**
     * Check extraction of properties for named connectors.
     */
    @Ignore
    @Test
    public void testExtractionOfConnectorProperties() {
        final String name1 = "abc";
        final String name2 = "def";

        final String prop1Key1 = "p1key1";
        final String prop1Key2 = "p1key2";
        final String prop2Key1 = "p2key1";
        final String prop2Key2 = "p2key2";

        final String prop1Value1 = "p1Val1";
        final String prop1Value2 = "p1Val2";
        final String prop2Value1 = "p2Va1";
        final String prop2Value2 = "p2Val2";

        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name1 + "." + prop1Key1, prop1Value1);
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name1 + "." + prop1Key2, prop1Value2);
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name2 + "." + prop2Key1, prop2Value1);
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name2 + "." + prop2Key2, prop2Value2);

        final Map<String, String> properties1 = ConnectorFactory.getInstance().getPropertiesForConnectorConfigurationName(name1);

        MatcherAssert.assertThat(properties1.keySet(), Matchers.containsInAnyOrder(prop1Key1, prop1Key2));
        MatcherAssert.assertThat(properties1.keySet(), Matchers.not(Matchers.containsInAnyOrder(prop2Key1, prop2Key2)));
        MatcherAssert.assertThat(properties1.get(prop1Key1), Matchers.equalTo(prop1Value1));
        MatcherAssert.assertThat(properties1.get(prop1Key2), Matchers.equalTo(prop1Value2));

    }

    /**
     * Tests extraction of connector names from system properties.
     */
    @Test
    public void testExtractionOfConnectorNames() {

        final String name1 = "abc";
        final String name2 = "def";
        final String name3 = "ghi";

        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name1 + "."
                + TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_CONNECTOR_TYPE, "class1");
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name2 + "."
                + TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_CONNECTOR_TYPE, "class2");
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name3 + "."
                + TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_CONNECTOR_TYPE, "class3");

        final Set<String> names = ConnectorFactory.getInstance().getConnectorConfigurationNames();

        MatcherAssert.assertThat(names, Matchers.containsInAnyOrder(name1, name2, name3));

    }

    /**
     * Check creation of connector for well known connector.
     */
    @Test
    public void testCreationOfWellKnownLogConnector() {
        final String name1 = "abc";

        final String prop1Key1 = "p1key1";
        final String prop1Key2 = "p1key2";

        final String prop1Value1 = "p1Val1";
        final String prop1Value2 = "p1Val2";

        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name1 + "."
                + TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_CONNECTOR_TYPE, LogConnector.class.getName());
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name1 + "." + prop1Key1, prop1Value1);
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name1 + "." + prop1Key2, prop1Value2);

        final Connector connector = ConnectorFactory.getInstance().createConnector(name1);

        MatcherAssert.assertThat(connector, Matchers.notNullValue());
        MatcherAssert.assertThat(connector, Matchers.instanceOf(LogConnector.class));

    }

    /**
     * Check creation of connector with a cononical classname.
     */
    @Test
    public void testCreationOfConnectorWithCanonicalClassName() {
        final String name1 = "abc";

        final String prop1Key1 = "p1key1";
        final String prop1Key2 = "p1key2";

        final String prop1Value1 = "p1Val1";
        final String prop1Value2 = "p1Val2";

        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name1 + "."
                + TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_CONNECTOR_TYPE, LogConnector.class.getCanonicalName());
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name1 + "." + prop1Key1, prop1Value1);
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name1 + "." + prop1Key2, prop1Value2);

        final Connector connector = ConnectorFactory.getInstance().createConnector(name1);

        MatcherAssert.assertThat(connector, Matchers.notNullValue());
        MatcherAssert.assertThat(connector, Matchers.instanceOf(LogConnector.class));

    }

    @Ignore
    @Test
    public void testHttpConnector() {

        final Map<String, String> map = new HashMap<String, String>();
        map.put(HttpConnector.PROPERTY_URL, "https://www.google.de");

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
