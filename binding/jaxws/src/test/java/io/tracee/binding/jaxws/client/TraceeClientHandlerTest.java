package io.tracee.binding.jaxws.client;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.transport.SoapHeaderTransport;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TraceeClientHandlerTest {

	private TraceeBackend backend = spy(SimpleTraceeBackend.createNonLoggingAllPermittingBackend());

	private final TraceeClientHandler unit = new TraceeClientHandler(backend);
	private SOAPMessageContext messageContext = mock(SOAPMessageContext.class);

	private SOAPMessage message;

	@Before
	public void setup() throws SOAPException {
		message = spy(MessageFactory.newInstance().createMessage());
		when(messageContext.getMessage()).thenReturn(message);
	}

	@Test
	public void faultsShouldBeHandledWithoutMessageInteraction() throws SOAPException {
		assertThat(unit.handleFault(messageContext), is(true));
		verify(message, never()).getSOAPHeader();
	}

	@Test
	public void skipProcessingWithoutErrorIfNoTpicHeaderIsInMessage() throws SOAPException {
		unit.handleIncoming(messageContext);
		assertThat(backend.isEmpty(), is(true));
	}

	@Test
	public void skipProcessingWithoutErrorIfNoSoapHeaderIsInMessage() throws SOAPException {
		when(message.getSOAPHeader()).thenReturn(null);
		unit.handleIncoming(messageContext);
		assertThat(backend.isEmpty(), is(true));
	}

	@Test
	public void catchExceptionOnReadTpicHeader() throws SOAPException {
		when(message.getSOAPHeader()).thenThrow(SOAPException.class);
		unit.handleIncoming(messageContext);
		assertThat(backend.isEmpty(), is(true));
	}

	@Test
	public void readSoapHeaderIntoToBackend() throws SOAPException, JAXBException {
		final Map<String, String> context = new HashMap<String, String>();
		context.put("abc", "123");

		new SoapHeaderTransport().renderSoapHeader(context, message.getSOAPHeader());
		when(messageContext.getMessage()).thenReturn(message);

		unit.handleIncoming(messageContext);
		assertThat(backend.copyToMap(), hasEntry("abc", "123"));
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
	public void renderBackendToSoapHeader() throws SOAPException {
		backend.put("my header", "Wow!");
		unit.handleOutgoing(messageContext);
		final NodeList tpicElements = message.getSOAPHeader().getElementsByTagNameNS(TraceeConstants.SOAP_HEADER_NAMESPACE, TraceeConstants.SOAP_HEADER_NAME);
		assertThat(tpicElements.getLength(), is(1));
		final Node tpicEntry = tpicElements.item(0).getChildNodes().item(0);
		assertThat(tpicEntry.getLocalName(), is("entry"));
		assertThat(tpicEntry.getAttributes().getNamedItem("key").getNodeValue(), is("my header"));
		assertThat(tpicEntry.getChildNodes().item(0).getNodeValue(), is("Wow!"));
	}

	@Test
	public void skipProcessingIfBackendIsEmpty() throws SOAPException {
		unit.handleOutgoing(messageContext);
		verify(message, never()).getSOAPHeader();
	}
}
