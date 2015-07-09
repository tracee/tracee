package io.tracee.binding.cxf.interceptor;

import io.tracee.testhelper.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.transport.jaxb.TpicMap;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class OutgoingSoapMessageTest {

	private final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
	private final Interceptor<Message> outInterceptor = new TraceeResponseOutInterceptor(backend);
	private final SoapMessage soapMessage = spy(new SoapMessage(new MessageImpl()));

	@Before
	public void before() {
		when(soapMessage.getExchange()).thenReturn(mock(Exchange.class));
	}

	@Test
	public void shouldHandleSoapMessageWithoutSoapHeader() {
		outInterceptor.handleMessage(soapMessage);
		assertThat(soapMessage.getHeader(TraceeConstants.SOAP_HEADER_QNAME), is(nullValue()));
	}

	@Test
	public void shouldAddHeaderWithDataBindingToSoapMessage() throws JAXBException {
		backend.put("mySoapContext", "mySoapContextValue");
		final Map<String,String> expectedHeaderFields = backend.copyToMap();
		outInterceptor.handleMessage(soapMessage);

		final Header tpicHeader = soapMessage.getHeaders().get(0);
		assertThat(tpicHeader.getName(), is(TraceeConstants.SOAP_HEADER_QNAME));
		assertThat(tpicHeader.getObject(), instanceOf(TpicMap.class));
		assertThat(((TpicMap) tpicHeader.getObject()), is(TpicMap.wrap(expectedHeaderFields)));
	}

	@Test
	public void shouldClearTheBackendAfterHandleMessage() throws JAXBException {
		backend.put("foo", "bar");
		outInterceptor.handleMessage(soapMessage);
		assertThat(backend.copyToMap(), equalTo(Collections.<String,String>emptyMap()));
	}
}
