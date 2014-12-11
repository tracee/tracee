package io.tracee.cxf.interceptor;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.transport.jaxb.TpicMap;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static org.hamcrest.Matchers.instanceOf;
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
		assertThat(soapMessage.getHeader(TraceeConstants.SOAP_HEADER_QNAME), is(nullValue()));
	}

	@Test
	public void shouldAddHeaderWithDataBindingToSoapMessage() throws JAXBException {
		backend.put("mySoapContext", "mySoapContextValue");
		outInterceptor.handleMessage(soapMessage);

		final Header tpicHeader = soapMessage.getHeaders().get(0);
		assertThat(tpicHeader.getName(), is(TraceeConstants.SOAP_HEADER_QNAME));
		assertThat(tpicHeader.getObject(), instanceOf(TpicMap.class));
		assertThat(((TpicMap) tpicHeader.getObject()), is(TpicMap.wrap(backend)));
	}
}
