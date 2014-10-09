package io.tracee.cxf.client;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.cxf.interceptor.TraceeInInterceptor;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class IncomingSoapMessageTest {

	private static final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();

	private Interceptor<Message> inInterceptor;

	private final SoapMessage soapMessage = new SoapMessage(new MessageImpl());

	@Before
	public void onSetup() throws Exception {
		inInterceptor = new TraceeInInterceptor(backend);
	}

	@Test
	public void shouldHandleSoapMessageWithoutSoapHeader() {
		inInterceptor.handleMessage(soapMessage);
		assertThat(backend.isEmpty(), is(true));
	}

	@Test
	@Ignore
	public void shouldHandleMessageWithoutTraceeHeader() {
		final HashMap<String, String> contextMap = new HashMap<String, String>();
		contextMap.put("mySoapKey", "mySoapContextValue");
		soapMessage.getHeaders().add(new Header(TraceeConstants.TRACEE_SOAP_HEADER_QNAME, contextMap, new JAXBDataBinding()));

		inInterceptor.handleMessage(soapMessage);
		assertThat(backend.get("mySoapKey"), is("mySoapContextValue"));
	}
}
