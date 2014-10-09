package io.tracee.cxf.client;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.cxf.interceptor.TraceeOutInterceptor;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

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
		outInterceptor = new TraceeOutInterceptor(backend);
	}

	@Test
	public void shouldHandleSoapMessageWithoutSoapHeader() {
		outInterceptor.handleMessage(soapMessage);
		assertThat(soapMessage.getHeader(TRACEE_SOAP_HEADER_QNAME), is(nullValue()));
	}

	@Test
	@Ignore
	public void renderContextToSoapHeader() {
		backend.put("mySoapContext", "mySoapContextValue");
		outInterceptor.handleMessage(soapMessage);
		final Map<Object, Object> contextMap = CastUtils.cast((Map<?, ?>) soapMessage.getHeader(TRACEE_SOAP_HEADER_QNAME).getObject());
		assertThat((String) contextMap.get("mySoapContext"), is("mySoapContextValue"));
	}

}
