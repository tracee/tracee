package de.holisticon.util.tracee.contextlogger.builder;

import de.holisticon.util.tracee.contextlogger.Connector;
import de.holisticon.util.tracee.contextlogger.TraceeContextLoggerConstants;
import de.holisticon.util.tracee.contextlogger.connector.LogConnector;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Test for {@link de.holisticon.util.tracee.contextlogger.builder.ConnectorFactory}.
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

        final Set<String> names = new ConnectorFactory().getConnectorConfigurationNames();

        assertThat(names, containsInAnyOrder(name1, name2, name3));

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

        final Connector connector = new ConnectorFactory().createConnector(name1);

        assertThat(connector, notNullValue());
        assertThat(connector, instanceOf(LogConnector.class));

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

        final Connector connector = new ConnectorFactory().createConnector(name1);

        assertThat(connector, notNullValue());
        assertThat(connector, instanceOf(LogConnector.class));

    }


}
