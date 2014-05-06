package io.tracee.jaxws.client;


import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.xml.ws.handler.PortInfo;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasItem;

public class TraceeClientHandlerResolverTest {

	private final PortInfo portInfo = Mockito.mock(PortInfo.class);
	private final TraceeClientHandlerResolver unit = new TraceeClientHandlerResolver();

	@Test
	public void testGetHandlerChain() throws Exception {
		Assert.assertThat(unit.getHandlerChain(portInfo), hasItem(any(TraceeClientHandler.class)));
	}
}
