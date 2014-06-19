package io.tracee.contextlogger;

import io.tracee.contextlogger.connector.LogConnector;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Test for {@link io.tracee.contextlogger.ConnectorFactory}.
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

        final Properties properties = new Properties();
        properties.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name1 + "."
                + TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_CONNECTOR_TYPE, "class1");
        properties.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name2 + "."
                + TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_CONNECTOR_TYPE, "class2");
        properties.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name3 + "."
                + TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_CONNECTOR_TYPE, "class3");


        final Set<String> names = new ConnectorFactory() {
            @Override
            protected Properties getSystemProperties() {
                return properties;
            }
        }.getConnectorConfigurationNames();

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

        final Properties properties = new Properties();
        properties.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name1 + "."
                + TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_CONNECTOR_TYPE, LogConnector.class.getName());
        properties.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name1 + "." + prop1Key1, prop1Value1);
        properties.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name1 + "." + prop1Key2, prop1Value2);

        final Connector connector = new ConnectorFactory() {
            @Override
            protected Properties getSystemProperties() {
                return properties;
            }
        }.createConnector(name1);

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

        final Properties properties = new Properties();
        properties.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name1 + "."
                + TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_CONNECTOR_TYPE, LogConnector.class.getCanonicalName());
        properties.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name1 + "." + prop1Key1, prop1Value1);
        properties.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name1 + "." + prop1Key2, prop1Value2);

        final Connector connector = new ConnectorFactory() {
            @Override
            protected Properties getSystemProperties() {
                return properties;
            }
        }.createConnector(name1);

        assertThat(connector, notNullValue());
        assertThat(connector, instanceOf(LogConnector.class));

    }


    /**
     * Check extraction of properties for named connectors.
     */
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

        final Properties properties = new Properties();
        properties.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name1 + "." + prop1Key1, prop1Value1);
        properties.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name1 + "." + prop1Key2, prop1Value2);
        properties.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name2 + "." + prop2Key1, prop2Value1);
        properties.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX + name2 + "." + prop2Key2, prop2Value2);

        final Map<String, String> properties1 = new ConnectorFactory() {
            @Override
            protected Properties getSystemProperties() {
                return properties;
            }
        }.getPropertiesForConnectorConfigurationName(name1);

        MatcherAssert.assertThat(properties1.keySet(), Matchers.containsInAnyOrder(prop1Key1, prop1Key2));
        MatcherAssert.assertThat(properties1.keySet(), Matchers.not(Matchers.containsInAnyOrder(prop2Key1, prop2Key2)));
        MatcherAssert.assertThat(properties1.get(prop1Key1), Matchers.equalTo(prop1Value1));
        MatcherAssert.assertThat(properties1.get(prop1Key2), Matchers.equalTo(prop1Value2));

    }

}
