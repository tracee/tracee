package io.tracee.jaxws.container;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.transport.SoapHeaderTransport;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Collections;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TraceeServerHandlerTest {

	private final TraceeBackend backend = spy(SimpleTraceeBackend.createNonLoggingAllPermittingBackend());
	private final SoapHeaderTransport soapHeaderTransport = mock(SoapHeaderTransport.class);
	private final TraceeServerHandler unit = new TraceeServerHandler(backend, soapHeaderTransport);
	private final SOAPMessageContext soapMessageContext = mock(SOAPMessageContext.class);
	private final SOAPMessage soapMessage = mock(SOAPMessage.class);
	private final SOAPPart soapPart = mock(SOAPPart.class);
	private final SOAPEnvelope soapEnvelope = mock(SOAPEnvelope.class);
	private final SOAPHeader soapHeader = mock(SOAPHeader.class);

	@Before
	public void setUp() throws SOAPException {
		when(soapMessageContext.getMessage()).thenReturn(soapMessage);
		when(soapMessage.getSOAPPart()).thenReturn(soapPart);
		when(soapPart.getEnvelope()).thenReturn(soapEnvelope);
		when(soapEnvelope.getHeader()).thenReturn(soapHeader);
	}

	@Test
	public void testHandleIncoming() throws JAXBException {
		when(soapHeaderTransport.parseSoapHeader(eq(soapHeader))).thenReturn(Collections.singletonMap("FOO","BAR"));
		unit.handleIncoming(soapMessageContext);
		verify(backend).putAll(Collections.singletonMap("FOO", "BAR"));
	}

	@Test
	public void testHandleOutgoing() throws SOAPException, JAXBException {
		unit.handleOutgoing(soapMessageContext);
		verify(soapHeaderTransport).renderSoapHeader(eq(backend), eq(soapHeader));
	}

	@Test
	public void testHandleOutgoingHasCalledClearInTheEnd() throws SOAPException {
		unit.handleOutgoing(soapMessageContext);
		verify(backend).clear();
	}
}
