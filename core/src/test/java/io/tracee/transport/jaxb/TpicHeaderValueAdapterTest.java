package io.tracee.transport.jaxb;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;

public class TpicHeaderValueAdapterTest {

	private static final String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
			"<ns2:TPIC xmlns:ns2=\"https://github.com/tracee/tracee\">\n" +
			"    <values>\n" +
			"        <value key=\"traceeRequestId\">1234567890</value>\n" +
			"        <value key=\"traceeSessionId\">ABCDEFGHIJ</value>\n" +
			"    </values>\n" +
			"</ns2:TPIC>\n";

	private JAXBContext jaxbContext;

	@Before
	public void setUp() throws Exception {
		jaxbContext = JAXBContext.newInstance(TpicMap.class);
	}

	@Test
	public void marshallRequestAndSessionId() throws JAXBException {
		final Map<String, String> context = new HashMap<String, String>();
		context.put(TraceeConstants.REQUEST_ID_KEY, "1234567890");
		context.put(TraceeConstants.SESSION_ID_KEY, "ABCDEFGHIJ");

		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		final StringWriter writer = new StringWriter();
		marshaller.marshal(TpicMap.wrap(context), writer);
		assertThat(writer.toString(), is(XML));
	}

	@Test
	public void unmarshallRequestAndSessionId() throws JAXBException {
		final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		final Map<String, String> context = ((TpicMap) unmarshaller.unmarshal(new StringReader(XML))).unwrap();
		assertThat(context, Matchers.hasEntry(TraceeConstants.REQUEST_ID_KEY, "1234567890")) ;
		assertThat(context, Matchers.hasEntry(TraceeConstants.SESSION_ID_KEY, "ABCDEFGHIJ")) ;
	}
}
