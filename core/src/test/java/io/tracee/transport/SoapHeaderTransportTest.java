package io.tracee.transport;

import io.tracee.TraceeConstants;
import io.tracee.transport.jaxb.TpicMap;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SoapHeaderTransportTest {

	private final SoapHeaderTransport unit = new SoapHeaderTransport();
	private SOAPMessage soapMessage;

	@Before
	public void setUp() throws SOAPException {
		this.soapMessage = MessageFactory.newInstance().createMessage();
	}

	@Test
	public void renderTpicHeaderToSoapMessage() throws SOAPException, JAXBException {
		final Map<String, String> context = Collections.singletonMap("FOO", "BAR");
		final SOAPHeader soapHeader = soapMessage.getSOAPHeader();

		unit.renderSoapHeader(context, soapHeader);
		final Iterator soapHeaders = soapHeader.getChildElements(TraceeConstants.SOAP_HEADER_QNAME);

		final Element tpicHeader = (Element) soapHeaders.next();
		assertThat(tpicHeader.getLocalName(), is(TraceeConstants.SOAP_HEADER_NAME));
		assertThat(tpicHeader.getNamespaceURI(), is(TraceeConstants.SOAP_HEADER_NAMESPACE));
		assertThat(tpicHeader.getFirstChild().getLocalName(), is("entry"));
		assertThat(tpicHeader.getFirstChild().getAttributes().getNamedItem("key").getNodeValue(), is("FOO"));
		assertThat(tpicHeader.getFirstChild().getFirstChild().getNodeValue(), is("BAR"));
	}

	@Test
	public void testParse() throws SOAPException, JAXBException {
		final TpicMap tpicMap = TpicMap.wrap(Collections.singletonMap("FOooo", "Bar2"));
		JAXBContext.newInstance(TpicMap.class).createMarshaller().marshal(tpicMap, soapMessage.getSOAPHeader());

		final Map<String, String> parse = unit.parseSoapHeader(soapMessage.getSOAPHeader());
		assertThat(parse, hasEntry("FOooo", "Bar2"));
	}

	@Test
	public void readRealXmlMessageAndParseHeader() throws Exception {
		final InputStream xmlDoc = ClassLoader.getSystemResourceAsStream("io/tracee/transport/jaxws-output.xml");

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setNamespaceAware(true);
		Document doc = dbFactory.newDocumentBuilder().parse(xmlDoc);
		Element envelope = (Element) doc.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Envelope").item(0);
		Element header = (Element) envelope.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Header").item(0);
		Map<String, String> parsedContextParams = unit.parseSoapHeader(header);
		assertThat(parsedContextParams, hasEntry(TraceeConstants.REQUEST_ID_KEY, "ANU0N88T6YASTEVHN9VK0HJ75SXB87ZQ"));
	}
}
