package de.holisticon.util.tracee.contextlogger.connector;

import de.holisticon.util.tracee.contextlogger.TraceeContextLoggerConstants;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Set;

/**
 * Test for ConnectorFactory.
 * Created by Tobias Gindler, holisticon AG on 14.02.14.
 */
public class ConnectorFactoryTest {

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


}
