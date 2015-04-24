package io.tracee.transport;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.transport.jaxb.TpicMap;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SoapHeaderTransportTest {

	private final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
	private final SoapHeaderTransport unit = new SoapHeaderTransport(backend.getLoggerFactory());
	private SOAPMessage soapMessage;

	@Before
	public void setUp() throws SOAPException {
		this.soapMessage = MessageFactory.newInstance().createMessage();
	}

	@Test
	public void renderTpicHeaderToSoapMessage() throws SOAPException {
		final Map<String, String> context = Collections.singletonMap("FOO", "BAR");
		final SOAPHeader soapHeader = soapMessage.getSOAPHeader();

		unit.renderSoapHeader(context, soapHeader);
		final Iterator soapHeaders = soapHeader.getChildElements(TraceeConstants.SOAP_HEADER_QNAME);

		final Element tpicHeader = (Element) soapHeaders.next();
		assertThat(tpicHeader.getLocalName(), is(TraceeConstants.TPIC_HEADER));
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
	public void parseSoapHeaderToEmptyMapIfNotParsable() throws SOAPException {
		assertThat(unit.parseSoapHeader(soapMessage.getSOAPHeader()), equalTo(Collections.<String, String>emptyMap()));
	}

	@Test
	public void parseTpicHeaderToEmptyMapIfNotParsable() throws SOAPException {
		assertThat(unit.parseTpicHeader(soapMessage.getSOAPHeader()), equalTo(Collections.<String, String>emptyMap()));
	}

	@Test
	public void readRealXmlMessageAndParseHeader() throws ParserConfigurationException, IOException, SAXException {
		final InputStream xmlDoc = ClassLoader.getSystemResourceAsStream("io/tracee/transport/jaxws-output.xml");

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setNamespaceAware(true);
		Document doc = dbFactory.newDocumentBuilder().parse(xmlDoc);
		Element envelope = (Element) doc.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Envelope").item(0);
		Element header = (Element) envelope.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Header").item(0);
		Map<String, String> parsedContextParams = unit.parseSoapHeader(header);
		assertThat(parsedContextParams, hasEntry(TraceeConstants.INVOCATION_ID_KEY, "ANU0N88T6YASTEVHN9VK0HJ75SXB87ZQ"));
	}

	@Test
	public void renderTpicContextToResult() {
		final Map<String, String> context = new HashMap<String, String>();
		context.put("myKey", "myValue");
		context.put("myKey2", "myValue2");

		final StringWriter writer = new StringWriter();
		unit.renderSoapHeader(context, new StreamResult(writer));
		assertThat(writer.toString(), containsString("<entry key=\"myKey\">myValue</entry>"));
		assertThat(writer.toString(), containsString("<entry key=\"myKey2\">myValue2</entry>"));
	}

	@Test
	public void returnEmptyMapIfSourceIsNull() {
		final Map<String, String> stringStringMap = unit.parseTpicHeader((Source) null);
		assertThat(stringStringMap.size(), is(0));
	}

	@Test
	public void parseSourceIntoTpicMap() {
		final String header = "<tpic xmlns=\"http://tracee.io/tpic/1.0\"><entry key=\"myKey\">myValue</entry></tpic>";

		final Map<String, String> contextMap = unit.parseTpicHeader(new StreamSource(new StringReader(header)));
		assertThat(contextMap, hasEntry("myKey", "myValue"));
		assertThat(contextMap.size(), is(1));
	}

	@Test
	public void serializeDeserializerTestWithResultAndSource() {
		final Map<String, String> initContext = new HashMap<String, String>();
		initContext.put("our key1", "our value1");
		initContext.put("our key2", "our value2");
		final StringWriter writer = new StringWriter();
		unit.renderSoapHeader(initContext, new StreamResult(writer));
		final Map<String, String> assertContext = unit.parseTpicHeader(new StreamSource(new StringReader(writer.toString())));
		assertThat(assertContext, Matchers.is(initContext));
	}
}
