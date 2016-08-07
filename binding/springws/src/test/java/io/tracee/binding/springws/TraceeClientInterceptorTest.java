package io.tracee.binding.springws;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import io.tracee.testhelper.FieldAccessUtil;
import io.tracee.testhelper.SimpleTraceeBackend;
import io.tracee.transport.SoapHeaderTransport;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.tracee.TraceeConstants.SOAP_HEADER_QNAME;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TraceeClientInterceptorTest {

	private TraceeBackend backend = spy(SimpleTraceeBackend.createNonLoggingAllPermittingBackend());

	private TraceeClientInterceptor unit = new TraceeClientInterceptor(backend, null);
	private MessageContext messageContext;

	@Before
	public void setup() {
		messageContext = mock(MessageContext.class);
		when(messageContext.getRequest()).thenReturn(mock(SoapMessage.class));
		when(messageContext.getResponse()).thenReturn(mock(SoapMessage.class));
		when(((SoapMessage) messageContext.getRequest()).getSoapHeader()).thenReturn(mock(SoapHeader.class));
		when(((SoapMessage) messageContext.getResponse()).getSoapHeader()).thenReturn(mock(SoapHeader.class));
	}

	@Test
	public void doNotAddTpicHeaderIfBackendIsEmpty() throws Exception {
		unit.handleRequest(messageContext);
		verify(((SoapMessage) messageContext.getResponse()).getSoapHeader(), never()).addHeaderElement(eq(SOAP_HEADER_QNAME));
	}

	@Test
	public void catchJaxbExceptionUponRenderingAndReturnWithoutException() throws Exception {
		backend.put("our key", "is our value");
		when(((SoapMessage) messageContext.getRequest()).getSoapHeader()).thenReturn(mock(SoapHeader.class));
		unit.handleRequest(messageContext);
		verify(((SoapMessage) messageContext.getRequest()).getSoapHeader()).getResult();
	}

	@Test
	public void renderTpicContextToRequest() throws Exception {
		backend.put("our key", "is our value");
		final StringWriter writer = new StringWriter();
		final SoapHeader soapHeader = mock(SoapHeader.class);
		when(((SoapMessage) messageContext.getRequest()).getSoapHeader()).thenReturn(soapHeader);
		when(soapHeader.getResult()).thenReturn(new StreamResult(writer));
		unit.handleRequest(messageContext);
		assertThat(writer.toString(), containsString("<entry key=\"our key\">is our value</entry>"));
	}

	@Test
	public void skipResponseHeaderProcessingIfNoSoapHeaderIsPresent() throws Exception {
		when(((SoapMessage) messageContext.getResponse()).getSoapHeader()).thenReturn(null);

		unit.handleResponse(messageContext);
		assertThat(backend.size(), is(0));
	}

	@Test
	public void skipResponseHeaderProcessingIfNoTpicHeaderIsPresent() throws Exception {
		final SoapHeader soapHeader = mock(SoapHeader.class);
		when(((SoapMessage) messageContext.getResponse()).getSoapHeader()).thenReturn(soapHeader);
		when(soapHeader.examineHeaderElements(eq(SOAP_HEADER_QNAME))).thenReturn(Collections.<SoapHeaderElement>emptyList().iterator());

		unit.handleResponse(messageContext);
		assertThat(backend.size(), is(0));
	}

	@Test
	public void parseTpicHeaderFromResponseToBackend() throws Exception {
		final Map<String, String> context = new HashMap<>();
		context.put("our key", "is our value");
		final StringResult result = new StringResult();
		new SoapHeaderTransport().renderSoapHeader(context, result);
		final Source source = new StringSource(result.toString());

		final SoapHeader soapHeader = mock(SoapHeader.class);
		when(((SoapMessage) messageContext.getResponse()).getSoapHeader()).thenReturn(soapHeader);
		final SoapHeaderElement element = mock(SoapHeaderElement.class);
		when(element.getSource()).thenReturn(source);
		when(soapHeader.examineHeaderElements(eq(SOAP_HEADER_QNAME))).thenReturn(Collections.singletonList(element).iterator());

		unit.handleResponse(messageContext);
		assertThat(backend.size(), is(1));
		assertThat(backend.copyToMap(), hasEntry("our key", "is our value"));
	}

	@Test
	public void parseTpicHeaderFromFaultResponseToBackend() throws Exception {
		final Map<String, String> context = new HashMap<>();
		context.put("our key", "is our value");
		final StringResult result = new StringResult();
		new SoapHeaderTransport().renderSoapHeader(context, result);
		final Source source = new StringSource(result.toString());

		final SoapHeader soapHeader = mock(SoapHeader.class);
		when(((SoapMessage) messageContext.getResponse()).getSoapHeader()).thenReturn(soapHeader);
		final SoapHeaderElement element = mock(SoapHeaderElement.class);
		when(element.getSource()).thenReturn(source);
		when(soapHeader.examineHeaderElements(eq(SOAP_HEADER_QNAME))).thenReturn(Collections.singletonList(element).iterator());

		unit.handleFault(messageContext);
		assertThat(backend.size(), is(1));
		assertThat(backend.copyToMap(), hasEntry("our key", "is our value"));
	}

	@Test
	public void skipProcessingWithWrongMessageTypeOnRequest() {
		when(messageContext.getRequest()).thenReturn(mock(WebServiceMessage.class));
		unit.handleRequest(messageContext);
		assertThat(backend.isEmpty(), is(true));
	}

	@Test
	public void skipProcessingWithWrongMessageTypeOnResponse() {
		when(messageContext.getResponse()).thenReturn(mock(WebServiceMessage.class));
		unit.handleResponse(messageContext);
		assertThat(backend.isEmpty(), is(true));
	}

	@Test
	public void defaultConstructorUsesDefaultProfile() {
		final TraceeClientInterceptor interceptor = new TraceeClientInterceptor();
		MatcherAssert.assertThat((String) FieldAccessUtil.getFieldVal(interceptor, "profile"), is(Profile.DEFAULT));
	}

	@Test
	public void defaultConstructorUsesTraceeBackend() {
		final TraceeClientInterceptor interceptor = new TraceeClientInterceptor();
		MatcherAssert.assertThat((TraceeBackend) FieldAccessUtil.getFieldVal(interceptor, "backend"), is(Tracee.getBackend()));
	}

	@Test
	public void constructorStoresProfileNameInternal() {
		final TraceeClientInterceptor interceptor = new TraceeClientInterceptor("testProf");
		MatcherAssert.assertThat((String) FieldAccessUtil.getFieldVal(interceptor, "profile"), is("testProf"));
	}
}
