package de.holisticon.util.tracee.configuration;

import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class PropertiesBasedTraceeFilterConfigurationTest {

	private Properties traceePropertiesFile = new Properties();
	private PropertiesBasedTraceeFilterConfiguration unit = new PropertiesBasedTraceeFilterConfiguration(traceePropertiesFile);


	@Test
	public void testShouldPropagatePositive() {
		traceePropertiesFile.put(PropertiesBasedTraceeFilterConfiguration.CONFIGURATION_KEY_PREFIX+ TraceeFilterConfiguration.MessageType.AsyncDispatch.name(),".*");
		assertTrue(unit.shouldPropagate("foo", TraceeFilterConfiguration.MessageType.AsyncDispatch));
	}

	@Test
	public void testShouldPropagateNegative() {
		traceePropertiesFile.put(PropertiesBasedTraceeFilterConfiguration.CONFIGURATION_KEY_PREFIX+ TraceeFilterConfiguration.MessageType.AsyncDispatch.name(),"a,b,c");
		assertFalse(unit.shouldPropagate("foo", TraceeFilterConfiguration.MessageType.AsyncDispatch));
	}

	@Test
	public void testShouldPropagateButNotConfigured() {
		assertFalse(unit.shouldPropagate("foo", TraceeFilterConfiguration.MessageType.AsyncDispatch));
	}

	@Test
	public void testGeneratedRequestIdLength() {
		traceePropertiesFile.setProperty(PropertiesBasedTraceeFilterConfiguration.GENERATE_REQUEST_ID, "1");
		assertThat(unit.generatedRequestIdLength(), equalTo(1));
	}

	@Test
	public void testGeneratedRequestIdNonNumericMeansZero() {
		traceePropertiesFile.setProperty(PropertiesBasedTraceeFilterConfiguration.GENERATE_REQUEST_ID, "false");
		assertThat(unit.generatedRequestIdLength(), equalTo(0));
	}

	@Test
	public void testGeneratedSessionIdLength() {
		traceePropertiesFile.setProperty(PropertiesBasedTraceeFilterConfiguration.GENERATE_SESSION_ID, "42");
		assertThat(unit.generatedSessionIdLength(), equalTo(42));
	}


}
