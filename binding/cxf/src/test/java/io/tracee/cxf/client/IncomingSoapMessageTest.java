package io.tracee.cxf.client;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.cxf.interceptor.TraceeInInterceptor;
import io.tracee.transport.SoapHeaderTransport;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import java.util.HashMap;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class IncomingSoapMessageTest {

	private static final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();

	private TraceeInInterceptor inInterceptor;

	private final SoapMessage soapMessage = new SoapMessage(new MessageImpl());

	@Before
	public void onSetup() throws Exception {
		backend.clear();
		inInterceptor = new TraceeInInterceptor(backend);
	}

	@Test
	public void shouldHandleSoapMessageWithoutSoapHeader() {
		inInterceptor.handleMessage(soapMessage);
		assertThat(backend.isEmpty(), is(true));
	}

	@Test
	public void shouldHandleMessageWithoutTraceeHeader() throws JAXBException {
		final HashMap<String, String> contextMap = new HashMap<String, String>();
		contextMap.put("mySoapKey", "mySoapContextValue");
		soapMessage.getHeaders().add(new Header(new QName(TraceeConstants.TRACEE_SOAP_HEADER_CONTEXT_URL, "SOME_OTHER"), contextMap, new JAXBDataBinding(HashMap.class)));

		inInterceptor.handleMessage(soapMessage);
		assertThat(backend.size(), is(0));
	}

	@Test
	public void shouldHandleMessageWithTraceeHeader() throws SOAPException {
		final HashMap<String, String> contextMap = new HashMap<String, String>();
		contextMap.put("mySoapKey", "mySoapContextValue");
		Element soapHeader = new SoapHeaderTransport().renderTo(contextMap);
		soapMessage.getHeaders().add(new Header(TraceeConstants.TRACEE_SOAP_HEADER_QNAME, soapHeader));

		inInterceptor.handleMessage(soapMessage);
		assertThat(backend.get("mySoapKey"), is("mySoapContextValue"));
	}
}
