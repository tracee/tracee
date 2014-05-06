package io.tracee.jaxws.protocol;

import io.tracee.jaxws.TraceeWsHandlerConstants;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class SoapHeaderTransportTest {

	private final SoapHeaderTransport unit = new SoapHeaderTransport();
	private final SOAPHeader soapHeader = mock(SOAPHeader.class);
	private final SOAPHeaderElement soapHeaderElement = mock(SOAPHeaderElement.class);
	private final SOAPElement soapElement = mock(SOAPElement.class);


	@Before
	public void setUp() throws SOAPException {
		when(soapHeader.addHeaderElement(any(QName.class))).thenReturn(soapHeaderElement);
		when(soapHeaderElement.addChildElement(any(String.class))).thenReturn(soapElement);
	}

	@Test
	public void testRenderTo() throws SOAPException {
		final Map<String, String> context = Collections.singletonMap("FOO", "BAR");
		unit.renderTo(context, soapHeader);
		verify(soapHeader).addHeaderElement(TraceeWsHandlerConstants.TRACEE_SOAP_HEADER_QNAME);
		verify(soapHeaderElement).addChildElement("FOO");
		verify(soapElement).setValue("BAR");
	}

	@Test
	public void testParse() throws SOAPException {

		final Node node = mock(Node.class);
		final NodeList nodeList = new SimpleNodeList(Collections.singleton(node));

		when(soapHeader.getElementsByTagName(eq(TraceeWsHandlerConstants.TRACEE_SOAP_HEADER_TAG_NAME))).thenReturn(nodeList);

		final Node childNode = mock(Node.class);
		when(childNode.getNodeName()).thenReturn("FOO");
		when(childNode.getTextContent()).thenReturn("BAR");
		final NodeList childNodeList = new SimpleNodeList(Collections.singleton(childNode));

		when(node.getChildNodes()).thenReturn(childNodeList);

		final Map<String, String> parsed = unit.parse(soapHeader);
		assertThat(parsed, hasEntry("FOO", "BAR"));
		assertThat(parsed.keySet(), hasSize(1));
	}

}