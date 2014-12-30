package io.tracee.binding.jaxws.container;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.transport.SoapHeaderTransport;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TraceeServerHandlerTest {

	private final TraceeBackend backend = spy(SimpleTraceeBackend.createNonLoggingAllPermittingBackend());
	private final SoapHeaderTransport soapHeaderTransport = mock(SoapHeaderTransport.class);
	private final TraceeServerHandler unit = new TraceeServerHandler(backend, soapHeaderTransport);
	private SOAPMessageContext messageContext = mock(SOAPMessageContext.class);

	private SOAPMessage message;

	@Before
	public void setup() throws SOAPException {
		message = spy(MessageFactory.newInstance().createMessage());
		when(messageContext.getMessage()).thenReturn(message);
	}

	@Test
	public void testHandleIncoming() throws JAXBException, SOAPException {
		when(soapHeaderTransport.parseSoapHeader(eq(message.getSOAPHeader()))).thenReturn(Collections.singletonMap("FOO", "BAR"));
		unit.handleIncoming(messageContext);
		verify(backend).putAll(Collections.singletonMap("FOO", "BAR"));
	}

	@Test
	public void generateRequestIdEvenWhenIncomingMessageCouldntParsed() throws SOAPException {
		when(message.getSOAPHeader()).thenThrow(SOAPException.class);
		unit.handleIncoming(messageContext);
		assertThat(backend.copyToMap(), hasKey(TraceeConstants.REQUEST_ID_KEY));
	}

	@Test
	public void testHandleOutgoing() throws SOAPException, JAXBException {
		backend.put("John", "Boy");
		final SOAPHeader soapHeader = message.getSOAPHeader();
		unit.handleOutgoing(messageContext);
		verify(soapHeaderTransport).renderSoapHeader(anyMapOf(String.class, String.class), eq(soapHeader));
	}

	@Test
	public void handleOutgoingIfBackendIsNotEmpty() throws SOAPException, JAXBException {
		backend.put("John", "Boy");
		final SOAPHeader header = message.getSOAPHeader();
		unit.handleOutgoing(messageContext);
		verify(soapHeaderTransport).renderSoapHeader(anyMapOf(String.class, String.class), eq(header));
	}

	@Test
	public void doNotHAndleOutgoingIfBackendIsEmpty() throws SOAPException, JAXBException {
		final SOAPHeader header = message.getSOAPHeader();
		unit.handleOutgoing(messageContext);
		verify(soapHeaderTransport, never()).renderSoapHeader(anyMapOf(String.class, String.class), eq(header));
	}

	@Test
	public void catchExceptionIfOutgoingTpicHeaderIsNotRenderable() throws SOAPException {
		when(message.getSOAPHeader()).thenThrow(SOAPException.class);
		unit.handleOutgoing(messageContext);
		assertThat(backend.isEmpty(), is(true));
	}

	@Test
	public void addSoapHeaderIfNooneIsOnMessage() throws SOAPException {
		backend.put("abcd", "12");
		when(message.getSOAPHeader()).thenReturn(null);
		when(message.getSOAPPart()).thenReturn(mock(SOAPPart.class));
		final SOAPEnvelope envelope = mock(SOAPEnvelope.class);
		when(message.getSOAPPart().getEnvelope()).thenReturn(envelope);

		unit.handleOutgoing(messageContext);
		verify(envelope).addHeader();
	}

	@Test
	public void testHandleOutgoingHasCalledClearInTheEnd() throws SOAPException {
		unit.handleOutgoing(messageContext);
		verify(backend).clear();
	}

	@Test
	public void handleFaults() throws SOAPException {
		backend.put("hey", "12");
		final boolean handleFault = unit.handleFault(messageContext);
		assertThat(handleFault, is(true));
		verify(message).getSOAPHeader();
	}
}
