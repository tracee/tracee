package io.tracee.configuration;

import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class TraceePropertiesFileLoaderTest {

	private final TraceePropertiesFileLoader unit = new TraceePropertiesFileLoader();

	final Properties properties;

	{
		try {
			properties = unit.loadTraceeProperties(TraceePropertiesFileLoader.TRACEE_DEFAULT_PROPERTIES_FILE);
		} catch (IOException e) {
			throw new IllegalStateException("Could not load default properties: " + e.getMessage(), e);
		}
	}

	@Test
	public void testLoadDefaultProperties() throws IOException {

		assertThat("property tracee.default.IncomingRequest", properties.getProperty("tracee.default.IncomingRequest"), not(isEmptyOrNullString()));
		assertThat("property tracee.default.OutgoingResponse", properties.getProperty("tracee.default.OutgoingResponse"), not(isEmptyOrNullString()));
		assertThat("property tracee.default.OutgoingRequest", properties.getProperty("tracee.default.OutgoingRequest"), not(isEmptyOrNullString()));
		assertThat("property tracee.default.IncomingResponse", properties.getProperty("tracee.default.IncomingResponse"), not(isEmptyOrNullString()));
		assertThat("property tracee.default.AsyncDispatch", properties.getProperty("tracee.default.AsyncDispatch"), not(isEmptyOrNullString()));
		assertThat("property tracee.default.AsyncProcess", properties.getProperty("tracee.default.AsyncProcess"), not(isEmptyOrNullString()));
		assertThat("property tracee.default.invocationIdLength", properties.getProperty("tracee.default.invocationIdLength"), not(isEmptyOrNullString()));
		assertThat("property tracee.default.sessionIdLength", properties.getProperty("tracee.default.sessionIdLength"), not(isEmptyOrNullString()));
	}

	@Test
	public void testLoadHideInboundProfile() throws IOException {
		assertThat("property tracee.profile.HideInbound.OutgoingResponse", properties.getProperty("tracee.profile.HideInbound.OutgoingResponse"), isEmptyString());
	}

	@Test
	public void testLoadHideOutboundProfile() throws IOException {
		assertThat("property tracee.profile.HideOutbound.OutgoingRequest", properties.getProperty("tracee.profile.HideOutbound.OutgoingRequest"), isEmptyString());
	}

	@Test
	public void testLoadDisableInboundProfile() throws IOException {
		assertThat("property tracee.profile.DisableInbound.IncomingRequest", properties.getProperty("tracee.profile.DisableInbound.IncomingRequest"), isEmptyString());
		assertThat("property tracee.profile.DisableInbound.OutgoingResponse", properties.getProperty("tracee.profile.DisableInbound.OutgoingResponse"), isEmptyString());
	}

	@Test
	public void testLoadDisableOutboundProfile() throws IOException {
		assertThat("property tracee.profile.DisableOutbound.OutgoingRequest", properties.getProperty("tracee.profile.DisableOutbound.OutgoingRequest"), isEmptyString());
		assertThat("property tracee.profile.DisableOutbound.IncomingResponse", properties.getProperty("tracee.profile.DisableOutbound.IncomingResponse"), isEmptyString());
	}

	@Test
	public void testLoadDisabledProfile() throws IOException {
		assertThat("property tracee.profile.Disabled.IncomingRequest", properties.getProperty("tracee.profile.Disabled.IncomingRequest"), isEmptyString());
		assertThat("property tracee.profile.Disabled.OutgoingResponse", properties.getProperty("tracee.profile.Disabled.OutgoingResponse"), isEmptyString());
		assertThat("property tracee.profile.Disabled.OutgoingRequest", properties.getProperty("tracee.profile.Disabled.OutgoingRequest"), isEmptyString());
		assertThat("property tracee.profile.Disabled.IncomingResponse", properties.getProperty("tracee.profile.Disabled.IncomingResponse"), isEmptyString());
		assertThat("property tracee.profile.Disabled.AsyncDispatch", properties.getProperty("tracee.profile.Disabled.AsyncDispatch"), isEmptyString());
		assertThat("property tracee.profile.Disabled.AsyncProcess", properties.getProperty("tracee.profile.Disabled.AsyncProcess"), isEmptyString());
	}

}
