package de.holisticon.util.tracee.jaxws.protocol;

import de.holisticon.util.tracee.jaxws.TraceeWsHandlerConstants;
import de.holisticon.util.tracee.transport.TransportSerialization;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class SoapHeaderTransport  {

	/**
	 * Parses a context map from a given soap header.
	 */
	public Map<String, String> parse(SOAPHeader header) {
		final NodeList nodeList = header.getElementsByTagName(
				TraceeWsHandlerConstants.TRACEE_SOAP_HEADER_TAG_NAME);
		final Map<String,String> context = new HashMap<String, String>();
		if (nodeList.getLength() > 0) {
			for (int i = 0; i < nodeList.getLength(); i++) {

				final Node node = nodeList.item(i);
				final NodeList childNodeList = node.getChildNodes();
				if (childNodeList.getLength() > 0) {

					for (int j = 0; j < childNodeList.getLength(); j++) {
						final Node childNode = childNodeList.item(j);
						final String attributeName = childNode.getNodeName();
						final String value = childNode.getTextContent();
						if (attributeName != null
								&& !attributeName.isEmpty()
								&& !"#text".equals(attributeName)) {
							context.put(attributeName, value);
						}
					}
				}

			}
		}
		return context;
	}

	/**
	 * Renders a given context map into a given soapHeader
	 */
	public void renderTo(Map<String, String> context, SOAPHeader soapHeader) throws SOAPException {
		// create soap header element for tracee entries
		final SOAPHeaderElement soapHeaderElement = soapHeader.addHeaderElement(
				TraceeWsHandlerConstants.TRACEE_SOAP_HEADER_QNAME);

		// loop over context attributes and add them to the header
		for (final Map.Entry<String, String> entry : context.entrySet()) {
			final SOAPElement traceeSoapHeaderElement = soapHeaderElement.addChildElement(entry.getKey());
			traceeSoapHeaderElement.setValue(entry.getValue());
		}
	}
}
