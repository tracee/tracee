package de.holisticon.util.tracee.contextlogger.builder;

import de.holisticon.util.tracee.contextlogger.TraceeContextLoggerConstants;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;

/**
 * Another Test for the connection factories.
 * Created by Tobias Gindler, holisticon AG on 20.02.14.
 */
public class AdditionalConnectorFactoryTest {

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

        final Map<String, String> properties1 = new ConnectorFactory().getPropertiesForConnectorConfigurationName(name1);

        MatcherAssert.assertThat(properties1.keySet(), Matchers.containsInAnyOrder(prop1Key1, prop1Key2));
        MatcherAssert.assertThat(properties1.keySet(), Matchers.not(Matchers.containsInAnyOrder(prop2Key1, prop2Key2)));
        MatcherAssert.assertThat(properties1.get(prop1Key1), Matchers.equalTo(prop1Value1));
        MatcherAssert.assertThat(properties1.get(prop1Key2), Matchers.equalTo(prop1Value2));

    }
}
