package io.tracee.binding.springws;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
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
import org.springframework.ws.soap.SoapHeaderException;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.tracee.TraceeConstants.INVOCATION_ID_KEY;
import static io.tracee.TraceeConstants.SOAP_HEADER_QNAME;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TraceeEndpointInterceptorTest {

	private TraceeBackend backend = spy(SimpleTraceeBackend.createNonLoggingAllPermittingBackend());

	private TraceeEndpointInterceptor unit = new TraceeEndpointInterceptor(backend, null);
	private MessageContext messageContext;

	@Before
	public void setup() {
		messageContext = mock(MessageContext.class);
		when(messageContext.getRequest()).thenReturn(mock(SoapMessage.class));
		when(messageContext.getResponse()).thenReturn(mock(SoapMessage.class));
		when(((SoapMessage) messageContext.getResponse()).getSoapHeader()).thenReturn(mock(SoapHeader.class));
	}

	@Test
	public void shouldCleanupBackendAfterInvocation() throws Exception {
		unit.afterCompletion(mock(MessageContext.class), new Object(), mock(Exception.class));
		verify(backend).clear();
	}

	@Test
	public void skipIncomingHeaderProcessingIfNoSoapHeaderIsPresentAndGenerateRequestId() throws Exception {
		when(((SoapMessage) messageContext.getRequest()).getSoapHeader()).thenReturn(null);

		unit.handleRequest(messageContext, new Object());
		assertThat(backend.copyToMap(), hasKey(INVOCATION_ID_KEY));
		assertThat(backend.size(), is(1));
	}

	@Test
	public void skipIncomingHeaderProcessingIfNoTpicHeaderIsPresentAndGenerateRequestId() throws Exception {
		final SoapHeader soapHeader = mock(SoapHeader.class);
		when(((SoapMessage) messageContext.getRequest()).getSoapHeader()).thenReturn(soapHeader);
		when(soapHeader.examineHeaderElements(eq(SOAP_HEADER_QNAME))).thenReturn(Collections.<SoapHeaderElement>emptyList().iterator());

		unit.handleRequest(messageContext, new Object());
		assertThat(backend.copyToMap(), hasKey(INVOCATION_ID_KEY));
		assertThat(backend.size(), is(1));
	}

	@Test
	public void parseTpicHeaderFromRequestToTraceeBackend() throws Exception {
		final Map<String, String> context = new HashMap<>();
		context.put("our key", "is our value");
		final StringResult result = new StringResult();
		new SoapHeaderTransport().renderSoapHeader(context, result);
		final Source source = new StringSource(result.toString());

		final SoapHeader soapHeader = mock(SoapHeader.class);
		when(((SoapMessage) messageContext.getRequest()).getSoapHeader()).thenReturn(soapHeader);
		final SoapHeaderElement element = mock(SoapHeaderElement.class);
		when(element.getSource()).thenReturn(source);
		when(soapHeader.examineHeaderElements(eq(SOAP_HEADER_QNAME))).thenReturn(singletonList(element).iterator());

		unit.handleRequest(messageContext, new Object());
		assertThat(backend.size(), is(2));
		assertThat(backend.copyToMap(), hasKey(INVOCATION_ID_KEY));
		assertThat(backend.copyToMap(), hasEntry("our key", "is our value"));
	}

	@Test
	public void handleSoapHeaderExceptionGraceful() {
		final SoapHeader soapHeader = mock(SoapHeader.class);
		when(soapHeader.examineHeaderElements(eq(SOAP_HEADER_QNAME))).thenThrow(new SoapHeaderException("test exception"));
		when(((SoapMessage) messageContext.getRequest()).getSoapHeader()).thenReturn(soapHeader);
		unit.handleRequest(messageContext, new Object());
		assertThat(backend.size(), is(1));
		assertThat(backend.copyToMap(), hasKey(INVOCATION_ID_KEY));
	}

	@Test
	public void doNotAddTpicHeaderToResponseIfBackendIsEmpty() throws Exception {
		unit.handleRequest(messageContext, new Object());
		verify(((SoapMessage) messageContext.getResponse()).getSoapHeader(), never()).addHeaderElement(eq(SOAP_HEADER_QNAME));
	}

	@Test
	public void catchJaxbExceptionOnRenderingAndReturnWithoutException() throws Exception {
		backend.put("our key", "is our value");
		when(((SoapMessage) messageContext.getResponse()).getSoapHeader()).thenReturn(mock(SoapHeader.class));
		unit.handleResponse(messageContext, new Object());
		verify(((SoapMessage) messageContext.getResponse()).getSoapHeader()).getResult();
	}

	@Test
	public void renderTpicContextToResultForResponses() throws Exception {
		backend.put("our key", "is our value");
		final StringWriter writer = new StringWriter();
		final SoapHeader soapHeader = mock(SoapHeader.class);
		when(((SoapMessage) messageContext.getResponse()).getSoapHeader()).thenReturn(soapHeader);
		when(soapHeader.getResult()).thenReturn(new StreamResult(writer));
		unit.handleResponse(messageContext, new Object());
		assertThat(writer.toString(), containsString("<entry key=\"our key\">is our value</entry>"));
	}

	@Test
	public void renderTpicContextToResultForFaults() throws Exception {
		backend.put("our key", "is our value");
		final StringWriter writer = new StringWriter();
		final SoapHeader soapHeader = mock(SoapHeader.class);
		when(((SoapMessage) messageContext.getResponse()).getSoapHeader()).thenReturn(soapHeader);
		when(soapHeader.getResult()).thenReturn(new StreamResult(writer));
		unit.handleFault(messageContext, new Object());
		assertThat(writer.toString(), containsString("<entry key=\"our key\">is our value</entry>"));
	}

	@Test
	public void skipProcessingWithWrongMessageTypeOnRequest() throws Exception {
		when(messageContext.getRequest()).thenReturn(mock(WebServiceMessage.class));
		unit.handleRequest(messageContext, new Object());
		assertThat(backend.copyToMap(), hasKey(TraceeConstants.INVOCATION_ID_KEY));
	}

	@Test
	public void skipProcessingWithWrongMessageTypeOnResponse() throws Exception {
		when(messageContext.getResponse()).thenReturn(mock(WebServiceMessage.class));
		unit.handleResponse(messageContext, new Object());
		assertThat(backend.isEmpty(), is(true));
	}

	@Test
	public void defaultConstructorUsesDefaultProfile() {
		final TraceeEndpointInterceptor interceptor = new TraceeEndpointInterceptor();
		MatcherAssert.assertThat((String) FieldAccessUtil.getFieldVal(interceptor, "profile"), is(TraceeFilterConfiguration.Profile.DEFAULT));
	}

	@Test
	public void defaultConstructorUsesTraceeBackend() {
		final TraceeEndpointInterceptor interceptor = new TraceeEndpointInterceptor();
		MatcherAssert.assertThat((TraceeBackend) FieldAccessUtil.getFieldVal(interceptor, "backend"), is(Tracee.getBackend()));
	}

	@Test
	public void constructorStoresProfileNameInternal() {
		final TraceeEndpointInterceptor interceptor = new TraceeEndpointInterceptor("testProf");
		MatcherAssert.assertThat((String) FieldAccessUtil.getFieldVal(interceptor, "profile"), is("testProf"));
	}
}
