package io.tracee.transport.jaxb;

import io.tracee.TraceeConstants;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TpicHeaderValueAdapterTest {

	private static final String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
			"<TPIC xmlns=\"http://tracee.io/tpic/1.0\">\n" +
			"    <entry key=\"TPIC.sessionId\">ABCDEFGHIJ</entry>\n" +
			"    <entry key=\"TPIC.invocationId\">1234567890</entry>\n" +
			"</TPIC>\n";

	private JAXBContext jaxbContext;

	@Rule
	public final ErrorCollector errorCollector = new ErrorCollector();

	@Before
	public void setUp() throws Exception {
		jaxbContext = JAXBContext.newInstance(TpicMap.class);
	}

	@Test
	public void marshallRequestAndSessionId() throws JAXBException {
		final Map<String, String> context = new HashMap<>();
		context.put(TraceeConstants.SESSION_ID_KEY, "ABCDEFGHIJ");
		context.put(TraceeConstants.INVOCATION_ID_KEY, "1234567890");

		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		final StringWriter writer = new StringWriter();
		marshaller.marshal(TpicMap.wrap(context), writer);
		final String xmlString = writer.toString();
		errorCollector.checkThat(xmlString, containsString("<TPIC xmlns=\"http://tracee.io/tpic/1.0\">"));
		errorCollector.checkThat(xmlString, endsWith("</TPIC>\n"));
		errorCollector.checkThat(xmlString, containsString("<entry key=\"TPIC.sessionId\">ABCDEFGHIJ</entry>"));
		errorCollector.checkThat(xmlString, containsString("<entry key=\"TPIC.invocationId\">1234567890</entry>"));
	}

	@Test
	public void unmarshallRequestAndSessionId() throws JAXBException {
		final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		final Map<String, String> context = ((TpicMap) unmarshaller.unmarshal(new StringReader(XML))).unwrapValues();
		assertThat(context, Matchers.hasEntry(TraceeConstants.INVOCATION_ID_KEY, "1234567890")) ;
		assertThat(context, Matchers.hasEntry(TraceeConstants.SESSION_ID_KEY, "ABCDEFGHIJ")) ;
	}

	@Test
	public void shouldTransferSpecialCharactersDueMarshallUnmarshall() throws JAXBException {
		final Map<String, String> context = new HashMap<>();
		context.put("ugly&<not>Xml-Fröndly", "H€@D-H|t$_K€Yb0\"<r>ß¿");
		Marshaller marshaller = jaxbContext.createMarshaller();
		final StringWriter writer = new StringWriter();
		marshaller.marshal(TpicMap.wrap(context), writer);
		System.out.println(writer.toString());

		final TpicMap mangledMap = (TpicMap) jaxbContext.createUnmarshaller().unmarshal(new StringReader(writer.toString()));
		assertThat(mangledMap.unwrapValues(), is(context));
	}
}
