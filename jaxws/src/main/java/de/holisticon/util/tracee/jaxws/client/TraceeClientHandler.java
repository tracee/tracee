package de.holisticon.util.tracee.jaxws.client;

import de.holisticon.util.tracee.TraceeLogger;
import de.holisticon.util.tracee.jaxws.AbstractTraceeHandler;
import de.holisticon.util.tracee.jaxws.TraceeWsHandlerConstants;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.HashSet;
import java.util.Set;

public class TraceeClientHandler extends AbstractTraceeHandler {

    private final TraceeLogger traceeLogger = getTraceeBackend().getLogger(TraceeClientHandler.class);

    public final Set<QName> getHeaders() {
        HashSet<QName> set = new HashSet<QName>();
        set.add(TraceeWsHandlerConstants.TRACEE_SOAP_HEADER_QNAME);
        return set;
    }

    @Override
    public final boolean handleFault(final SOAPMessageContext context) {
        return true;
    }

    protected final void handleInbound(final SOAPMessageContext context) {

        final SOAPMessage msg = context.getMessage();
        if (msg != null) {

            try {
                final SOAPEnvelope env = msg.getSOAPPart().getEnvelope();

                // get attribute names from cds and store them into the session
                final Set<String> existingAttributeNames = new HashSet<String>(
                        this.getTraceeBackend().getRegisteredKeys());

                // get soap header
                final SOAPHeader header = env.getHeader();
                if (header != null) {

                    // check wether tracee soap handler has been set or not
                    final NodeList nodeList = header.getElementsByTagName(
                            TraceeWsHandlerConstants.TRACEE_SOAP_HEADER_TAG_NAME);
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
                                            && !"#text".equals(attributeName)
                                            && !existingAttributeNames.contains(attributeName)) {

                                        this.getTraceeBackend().put(attributeName, value);

                                        // Add it, only first ocurrence of attribute!
                                        existingAttributeNames.add(attributeName);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (final SOAPException e) {
                e.printStackTrace();
                traceeLogger.error(
                        "TraceeClientHandler : Exception occurred during processing of inbound message.", e);
            }

        }
    }


    protected final void handleOutbound(final SOAPMessageContext context) {

        final SOAPMessage msg = context.getMessage();
        if (msg != null) {

            try {
                final SOAPEnvelope env = msg.getSOAPPart().getEnvelope();

                // get or create header
                SOAPHeader header = env.getHeader();
                if (header == null) {
                    header = env.addHeader();
                }

                // create soap header element for tracee entries

                final SOAPHeaderElement soapHeaderElement = header.addHeaderElement(
                        TraceeWsHandlerConstants.TRACEE_SOAP_HEADER_QNAME);

                // loop over mdc attributes and add them to the header
                for (final String attributeName : this.getTraceeBackend().getRegisteredKeys()) {

                    final SOAPElement traceeSoapHeaderElement = soapHeaderElement.addChildElement(attributeName);

                    final String attributeValue = this.getTraceeBackend().get(attributeName);
                    traceeSoapHeaderElement.setValue(attributeValue);

                }

                msg.saveChanges();

            } catch (final SOAPException e) {
                traceeLogger.error("TraceeClientHandler : Exception "
                        + "occurred during processing of outbound message.", e);
            }

            context.setMessage(msg);

        }
    }

}
