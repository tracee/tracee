package io.tracee.configuration;

import org.junit.Test;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.AsyncDispatch;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.AsyncProcess;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingResponse;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.valueOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

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
