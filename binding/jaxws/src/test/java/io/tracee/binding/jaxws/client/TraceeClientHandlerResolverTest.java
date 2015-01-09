package io.tracee.binding.jaxws.client;


import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.Mockito;

import javax.xml.ws.handler.PortInfo;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

public class TraceeClientHandlerResolverTest {

	private final PortInfo portInfo = Mockito.mock(PortInfo.class);
	private final TraceeClientHandlerResolver unit = new TraceeClientHandlerResolver();

	@Test
	public void testGetHandlerChain() throws Exception {
		final Matcher<Iterable<? super TraceeClientHandler>> matcher = hasItem(any(TraceeClientHandler.class));
		assertThat(unit.getHandlerChain(portInfo), matcher);
	}
}
