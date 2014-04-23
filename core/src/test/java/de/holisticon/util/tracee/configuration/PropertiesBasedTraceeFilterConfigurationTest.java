package de.holisticon.util.tracee.configuration;

import org.junit.Test;
import org.mockito.Mockito;

import static de.holisticon.util.tracee.configuration.PropertiesBasedTraceeFilterConfiguration.*;
import static de.holisticon.util.tracee.configuration.TraceeFilterConfiguration.Channel.AsyncDispatch;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class PropertiesBasedTraceeFilterConfigurationTest {

	private PropertyChain propertyChain = Mockito.mock(PropertyChain.class);
	private PropertiesBasedTraceeFilterConfiguration unit = new PropertiesBasedTraceeFilterConfiguration(propertyChain);


	@Test
	public void testShouldPropagatePositive() {
		when(propertyChain.getProperty(TRACEE_CONFIG_PREFIX + DEFAULT_PROFILE + AsyncDispatch.name())).thenReturn(".*");
		assertTrue(unit.shouldProcessParam("foo", AsyncDispatch));
	}

	@Test
	public void testShouldPropagateNegative() {
		when(propertyChain.getProperty(TRACEE_CONFIG_PREFIX + DEFAULT_PROFILE + AsyncDispatch.name())).thenReturn("a,b,c");
		assertFalse(unit.shouldProcessParam("foo", AsyncDispatch));
	}

	@Test
	public void testShouldAllowRegexInPatterns() {
		when(propertyChain.getProperty(TRACEE_CONFIG_PREFIX + DEFAULT_PROFILE + AsyncDispatch.name())).thenReturn("b[oa]+b");
		assertTrue(unit.shouldProcessParam("baab", AsyncDispatch));
		assertTrue(unit.shouldProcessParam("boob", AsyncDispatch));
	}

	@Test
	public void testShouldNotPropagateIfNothingIsConfigured() {
		assertFalse(unit.shouldProcessParam("foo", AsyncDispatch));
	}

	@Test
	public void testShouldProcessIfDisabledByConfiguration() {
		when(propertyChain.getProperty(TRACEE_CONFIG_PREFIX + DEFAULT_PROFILE + AsyncDispatch.name())).thenReturn(" \t\n");
		assertFalse(unit.shouldProcessContext(AsyncDispatch));
	}

	@Test
	public void testShouldProcessIfAnyPatternIsGiven() {
		when(propertyChain.getProperty(TRACEE_CONFIG_PREFIX + DEFAULT_PROFILE + AsyncDispatch.name())).thenReturn("a");
		assertTrue(unit.shouldProcessContext(AsyncDispatch));
	}

	@Test
	public void testGeneratedRequestIdLength() {
		when(propertyChain.getProperty(TRACEE_CONFIG_PREFIX + DEFAULT_PROFILE + GENERATE_REQUEST_ID)).thenReturn("1");
		assertThat(unit.generatedRequestIdLength(), equalTo(1));
	}

	@Test
	public void testGeneratedRequestIdNonNumericMeansZero() {
		when(propertyChain.getProperty(TRACEE_CONFIG_PREFIX + DEFAULT_PROFILE + GENERATE_REQUEST_ID)).thenReturn("false");
		assertThat(unit.generatedRequestIdLength(), equalTo(0));
	}

	@Test
	public void testGeneratedSessionIdLength() {
		when(propertyChain.getProperty((TRACEE_CONFIG_PREFIX + DEFAULT_PROFILE + GENERATE_SESSION_ID))).thenReturn("42");
		assertThat(unit.generatedSessionIdLength(), equalTo(42));
	}


}
