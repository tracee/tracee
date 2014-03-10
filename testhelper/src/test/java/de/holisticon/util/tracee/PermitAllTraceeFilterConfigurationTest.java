package de.holisticon.util.tracee;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static de.holisticon.util.tracee.configuration.TraceeFilterConfiguration.Channel.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
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
	public void testShouldAlwaysGenerateRequestId() {
		assertThat(unit.shouldGenerateRequestId(), equalTo(true));
	}

	@Test
	public void testGeneratedRequestIdWithLength32() {
		assertThat(unit.generatedRequestIdLength(), equalTo(32));
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
