package de.holisticon.util.tracee.jaxws.protocol;

import de.holisticon.util.tracee.jaxws.TraceeWsHandlerConstants;
import org.junit.Before;
import org.junit.Test;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import java.util.Collections;
import java.util.Map;

import static org.mockito.Matchers.any;
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
		final Map<String,String> context = Collections.singletonMap("FOO", "BAR");
		unit.renderTo(context,soapHeader);
		verify(soapHeader).addHeaderElement(TraceeWsHandlerConstants.TRACEE_SOAP_HEADER_QNAME);
		verify(soapHeaderElement).addChildElement("FOO");
		verify(soapElement).setValue("BAR");
	}
}