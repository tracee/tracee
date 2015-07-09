package io.tracee.testhelper;

import io.tracee.testhelper.PermitAllTraceeFilterConfiguration;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PermitAllTraceeFilterConfigurationTest {

	private PermitAllTraceeFilterConfiguration unit = new PermitAllTraceeFilterConfiguration();

	@Test
	public void testShouldProcessAllParam() {
		assertThat(unit.shouldProcessParam("arbitary param", AsyncProcess), equalTo(true));
		assertThat(unit.shouldProcessParam("another param", IncomingRequest), equalTo(true));
	}

	@Test
	public void testFilterDeniedParamsFiltersNothing() {
		final Map<String,String> arbitraryContext = new HashMap<String,String>();
		arbitraryContext.put("foo", "bar");
		arbitraryContext.put("anything", "else");
		MatcherAssert.assertThat(unit.filterDeniedParams(arbitraryContext, IncomingRequest), equalTo(arbitraryContext));
	}

	@Test
	public void testShouldProcessAllContext() {
		assertThat(unit.shouldProcessContext(IncomingRequest), equalTo(true));
		assertThat(unit.shouldProcessContext(OutgoingResponse), equalTo(true));
		assertThat(unit.shouldProcessContext(OutgoingRequest), equalTo(true));
		assertThat(unit.shouldProcessContext(IncomingResponse), equalTo(true));
		assertThat(unit.shouldProcessContext(AsyncDispatch), equalTo(true));
		assertThat(unit.shouldProcessContext(AsyncProcess), equalTo(true));
	}

	@Test
	public void testShouldAlwaysGenerateInvocationId() {
		assertThat(unit.shouldGenerateInvocationId(), equalTo(true));
	}

	@Test
	public void testGeneratedInvocationIdWithLength32() {
		assertThat(unit.generatedInvocationIdLength(), equalTo(32));
	}

	@Test
	public void testShouldAlwaysGenerateSessionId() {
		assertThat(unit.shouldGenerateSessionId(), equalTo(true));
	}

	@Test
	public void testGeneratedSessionIdWithLength32() {
		assertThat(unit.generatedSessionIdLength(), equalTo(32));
	}
}
