package io.tracee.configuration;

import org.junit.Test;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class TraceeFilterConfigurationTest {

	@Test
	public void shouldReturnIncomingRequestEnum() {
		assertThat(valueOf("IncomingRequest"), is(IncomingRequest));
	}

	@Test
	public void shouldReturnIncomingResponseEnum() {
		assertThat(valueOf("IncomingResponse"), is(IncomingResponse));
	}

	@Test
	public void shouldReturnOutgoingRequestEnum() {
		assertThat(valueOf("OutgoingRequest"), is(OutgoingRequest));
	}

	@Test
	public void shouldReturnOutgoingResponseEnum() {
		assertThat(valueOf("OutgoingResponse"), is(OutgoingResponse));
	}

	@Test
	public void shouldReturnAsyncDispatchEnum() {
		assertThat(valueOf("AsyncDispatch"), is(AsyncDispatch));
	}

	@Test
	public void shouldReturnAsyncProcessEnum() {
		assertThat(valueOf("AsyncProcess"), is(AsyncProcess));
	}
}
