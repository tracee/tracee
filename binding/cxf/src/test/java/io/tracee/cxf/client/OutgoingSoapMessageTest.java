package io.tracee.cxf.client;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.cxf.interceptor.TraceeOutInterceptor;
import io.tracee.transport.SoapHeaderTransport;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import java.util.Map;

import static io.tracee.TraceeConstants.TRACEE_SOAP_HEADER_QNAME;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OutgoingSoapMessageTest {

	private static final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();

	private Interceptor<Message> outInterceptor;

	private final SoapMessage soapMessage = new SoapMessage(new MessageImpl());

	@Before
	public void onSetup() throws Exception {
		backend.clear();
		outInterceptor = new TraceeOutInterceptor(backend);
	}

	@Test
	public void shouldHandleSoapMessageWithoutSoapHeader() {
		outInterceptor.handleMessage(soapMessage);
		assertThat(soapMessage.getHeader(TRACEE_SOAP_HEADER_QNAME), is(nullValue()));
	}

	@Test
	public void renderContextToSoapHeader() {
		backend.put("mySoapContext", "mySoapContextValue");
		outInterceptor.handleMessage(soapMessage);

		final Map<String, String> contextMap = new SoapHeaderTransport().parse((Element) soapMessage.getHeader(TRACEE_SOAP_HEADER_QNAME).getObject());

		assertThat(contextMap.get("mySoapContext"), is("mySoapContextValue"));
	}

}
