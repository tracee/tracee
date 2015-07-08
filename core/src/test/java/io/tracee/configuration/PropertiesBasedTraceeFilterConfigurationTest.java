package io.tracee.configuration;

import io.tracee.testhelper.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Map;

import static io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration.*;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.AsyncDispatch;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class PropertiesBasedTraceeFilterConfigurationTest {

	private PropertyChain propertyChain = Mockito.mock(PropertyChain.class);
	private TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
	private PropertiesBasedTraceeFilterConfiguration unit = new PropertiesBasedTraceeFilterConfiguration(propertyChain);

	@Test
	public void testShouldPropagatePositive() {
		when(propertyChain.getProperty(TRACEE_DEFAULT_PROFILE_PREFIX + AsyncDispatch.name())).thenReturn(".*");
		assertTrue(unit.shouldProcessParam("foo", AsyncDispatch));
	}

	@Test
	public void testShouldPropagateNegative() {
		when(propertyChain.getProperty(TRACEE_DEFAULT_PROFILE_PREFIX + AsyncDispatch.name())).thenReturn("a,b,c");
		assertFalse(unit.shouldProcessParam("foo", AsyncDispatch));
	}

	@Test
	public void testShouldAllowRegexInPatterns() {
		when(propertyChain.getProperty(TRACEE_DEFAULT_PROFILE_PREFIX + AsyncDispatch.name())).thenReturn("b[oa]+b");
		assertTrue(unit.shouldProcessParam("baab", AsyncDispatch));
		assertTrue(unit.shouldProcessParam("boob", AsyncDispatch));
	}

	@Test
	public void testShouldNotPropagateIfNothingIsConfigured() {
		assertFalse(unit.shouldProcessParam("foo", AsyncDispatch));
	}

	@Test
	public void testShouldProcessIfDisabledByConfiguration() {
		when(propertyChain.getProperty(TRACEE_DEFAULT_PROFILE_PREFIX + AsyncDispatch.name())).thenReturn(" \t\n");
		assertFalse(unit.shouldProcessContext(AsyncDispatch));
	}

	@Test
	public void testShouldProcessIfAnyPatternIsGiven() {
		when(propertyChain.getProperty(TRACEE_DEFAULT_PROFILE_PREFIX + AsyncDispatch.name())).thenReturn("a");
		assertTrue(unit.shouldProcessContext(AsyncDispatch));
	}

	@Test
	public void testGeneratedInvocationIdLength() {
		when(propertyChain.getProperty(TRACEE_DEFAULT_PROFILE_PREFIX + GENERATE_INVOCATION_ID)).thenReturn("1");
		assertThat(unit.generatedInvocationIdLength(), equalTo(1));
	}

	@Test
	public void testGeneratedInvocationIdNonNumericMeansZero() {
		when(propertyChain.getProperty(TRACEE_DEFAULT_PROFILE_PREFIX + GENERATE_INVOCATION_ID)).thenReturn("false");
		assertThat(unit.generatedInvocationIdLength(), equalTo(0));
	}

	@Test
	public void testGeneratedSessionIdLength() {
		when(propertyChain.getProperty((TRACEE_DEFAULT_PROFILE_PREFIX + GENERATE_SESSION_ID))).thenReturn("42");
		assertThat(unit.generatedSessionIdLength(), equalTo(42));
	}

	@Test
	public void testFilterDeniedParamsFiltersEverythingWithoutConfiguration() {
		final Map<String, String> unfiltered = Collections.singletonMap("Foo", "Bar");
		assertThat(unit.filterDeniedParams(unfiltered, Channel.IncomingRequest), equalTo(Collections.<String,String>emptyMap()));
	}

	@Test
	public void testFilterDeniedParamsPassesWhitelisted() {
		when(propertyChain.getProperty(TRACEE_DEFAULT_PROFILE_PREFIX + IncomingRequest.name())).thenReturn("Foo");
		final Map<String, String> unfiltered = Collections.singletonMap("Foo", "Bar");
		assertThat(unit.filterDeniedParams(unfiltered,Channel.IncomingRequest), equalTo(unfiltered));
	}
}
