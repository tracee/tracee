package io.tracee.transport;

import io.tracee.TraceeConstants;
import io.tracee.transport.jaxb.TpicMap;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.bind.*;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import java.util.HashMap;
import java.util.Map;

public class SoapHeaderTransport {

	private final JAXBContext jaxbContext;

	public SoapHeaderTransport() {
		try {
			this.jaxbContext = JAXBContext.newInstance(TpicMap.class);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	public Map<String, String> parseSoapHeader(Element soapHeader) throws JAXBException {
		final NodeList tpicHeaders = soapHeader.getElementsByTagNameNS(TraceeConstants.SOAP_HEADER_NAMESPACE, TraceeConstants.TPIC_HEADER);
		if (tpicHeaders != null && tpicHeaders.getLength() > 0) {
			return parseTpicHeader((Element) tpicHeaders.item(0));
		}
		return new HashMap<String, String>();
	}

	/**
	 * Parses a context map from a given soap header.
	 */
	public Map<String, String> parseTpicHeader(Element header) throws JAXBException {
		if (header != null && header.hasChildNodes()) {
			final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			final JAXBElement<TpicMap> unmarshal = unmarshaller.unmarshal(header, TpicMap.class);
			if (unmarshal != null) {
				return unmarshal.getValue().unwrapValues();
			}
		}
		return new HashMap<String, String>();
	}

	/**
	 * Renders a given context map into a given soapHeader.
	 */
	public void renderSoapHeader(Map<String, String> context, SOAPHeader soapHeader) throws SOAPException, JAXBException {
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.marshal(TpicMap.wrap(context), soapHeader);
	}
}
