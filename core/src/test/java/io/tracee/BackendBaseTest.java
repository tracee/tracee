package io.tracee;

import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Channel;
import org.junit.Test;
import org.mockito.Mockito;

import static io.tracee.configuration.TraceeFilterConfiguration.Profile.DISABLED;
import static io.tracee.configuration.TraceeFilterConfiguration.Profile.DISABLE_INBOUND;
import static io.tracee.configuration.TraceeFilterConfiguration.Profile.DISABLE_OUTBOUND;
import static io.tracee.configuration.TraceeFilterConfiguration.Profile.HIDE_INBOUND;
import static io.tracee.configuration.TraceeFilterConfiguration.Profile.HIDE_OUTBOUND;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class BackendBaseTest {

	private final BackendBase unit = Mockito.spy(BackendBase.class);

	@Test
	public void invocationIdShortcutShouldReturnTheInvocationIdIfSet() {
		when(unit.get(eq(TraceeConstants.INVOCATION_ID_KEY))).thenReturn("ourInvocationId");
		assertThat(unit.getInvocationId(), is("ourInvocationId"));
	}

	@Test
	public void invocationIdShortcutShouldReturnNullIfNoInvocationIdIsSet() {
		assertThat(unit.getInvocationId(), is(nullValue()));
	}

	@Test
	public void sessionIdShortcutShouldReturnTheSessionIdIfSet() {
		when(unit.get(eq(TraceeConstants.SESSION_ID_KEY))).thenReturn("ourSessionId");
		assertThat(unit.getSessionId(), is("ourSessionId"));
	}

	@Test
	public void sessionIdShortcutShouldReturnNullIfNoSessionIdIsSet() {
		assertThat(unit.getSessionId(), is(nullValue()));
	}

	@Test
	public void testLoadOverwrittenConfigurationValues() {
		assertThat(unit.getConfiguration().generatedInvocationIdLength(), equalTo(42));
	}

	@Test
	public void testLoadUserDefinedProfileFromProperties() {
		assertThat(unit.getConfiguration("FOO").shouldProcessParam("ANY", Channel.IncomingRequest), equalTo(true));
	}

	@Test
	public void testHideInboundProfile() {
		assertThat(unit.getConfiguration(HIDE_INBOUND).shouldProcessContext(Channel.OutgoingResponse), equalTo(false));
	}

	@Test
	public void testHideOutboundProfile() {
		assertThat(unit.getConfiguration(HIDE_OUTBOUND).shouldProcessContext(Channel.OutgoingRequest), equalTo(false));
	}

	@Test
	public void testDisableInboundProfile() {
		assertThat(unit.getConfiguration(DISABLE_INBOUND).shouldProcessContext(Channel.IncomingRequest), equalTo(false));
		assertThat(unit.getConfiguration(DISABLE_INBOUND).shouldProcessContext(Channel.OutgoingResponse), equalTo(false));
	}

	@Test
	public void testDisableOutboundProfile() {
		assertThat(unit.getConfiguration(DISABLE_OUTBOUND).shouldProcessContext(Channel.OutgoingRequest), equalTo(false));
		assertThat(unit.getConfiguration(DISABLE_OUTBOUND).shouldProcessContext(Channel.IncomingResponse), equalTo(false));
	}


	@Test
	public void testDisabledProfile() {
		assertThat(unit.getConfiguration(DISABLED).shouldProcessContext(Channel.AsyncDispatch), equalTo(false));
		assertThat(unit.getConfiguration(DISABLED).shouldProcessContext(Channel.AsyncProcess), equalTo(false));
		assertThat(unit.getConfiguration(DISABLED).shouldProcessContext(Channel.IncomingRequest), equalTo(false));
		assertThat(unit.getConfiguration(DISABLED).shouldProcessContext(Channel.IncomingResponse), equalTo(false));
		assertThat(unit.getConfiguration(DISABLED).shouldProcessContext(Channel.OutgoingRequest), equalTo(false));
		assertThat(unit.getConfiguration(DISABLED).shouldProcessContext(Channel.OutgoingResponse), equalTo(false));
	}

	@Test
	public void testNullProfile() {
		assertThat(unit.getConfiguration(null), is(not(nullValue())));
	}

	@Test
	public void testProfileCacheForEmptyOrNullProfile() {
		assertThat(unit.getConfiguration(), is(unit.getConfiguration(null)));
	}

	@Test
	public void testProfileCacheForGivenProfile() {
		final TraceeFilterConfiguration hideInboundConfiguration = unit.getConfiguration(HIDE_INBOUND);
		assertThat(unit.getConfiguration(HIDE_INBOUND), is(sameInstance(hideInboundConfiguration)));
	}

	@Test
	public void testProfileCacheForDifferenceProfiles() {
		final TraceeFilterConfiguration hideInboundConfiguration = unit.getConfiguration(HIDE_OUTBOUND);
		assertThat(unit.getConfiguration(HIDE_INBOUND), is(not(sameInstance(hideInboundConfiguration))));
	}
}
