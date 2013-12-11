package de.holisticon.util.tracee.jaxws.server;


import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.TraceeLogger;
import de.holisticon.util.tracee.jaxws.AbstractTraceeHandler;
import de.holisticon.util.tracee.jaxws.TraceeWsHandlerConstants;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TraceeServerHandler extends AbstractTraceeHandler {

    private final TraceeLogger traceeLogger = getTraceeBackend().getLogger(TraceeServerHandler.class);

    private String generateRandomHexString() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    protected final void handleInbound(SOAPMessageContext context) {
        try {
            final SOAPMessage msg = context.getMessage();
            final SOAPEnvelope env = msg.getSOAPPart().getEnvelope();

            // get soap header
            final SOAPHeader header = env.getHeader();
            if (header != null) {

                final Set<String> passedAttributes = new HashSet<String>();

                // check if tracee soap handler has been set or not
                final NodeList nodeList = header.getElementsByTagName(
                        TraceeWsHandlerConstants.TRACEE_SOAP_HEADER_TAG_NAME);
                if (nodeList.getLength() > 0) {

                    for (int i = 0; i < nodeList.getLength(); i++) {


                        final Node node = nodeList.item(i);
                        final NodeList childNodeList = node.getChildNodes();
                        if (childNodeList.getLength() > 0) {

                            for (int j = 0; j < childNodeList.getLength(); j++) {

                                final Node childNode = childNodeList.item(j);

                                NamedNodeMap namedNodeMap = childNode.getAttributes();
                                final String attributeName = childNode.getNodeName();
                                final String value = childNode.getTextContent();


                                if (attributeName != null
                                        && !attributeName.isEmpty()
                                        && !"#text".equals(attributeName)
                                        && !passedAttributes.contains(attributeName)) {
                                    this.getTraceeBackend().put(attributeName, value);
                                    passedAttributes.add(attributeName);
                                }

                            }
                        }

                    }
                }
            }

            // init undefined values

            // generate request id if it doesn't exist
            if (getTraceeBackend().get(TraceeConstants.REQUEST_ID_KEY) == null) {
                getTraceeBackend().put(TraceeConstants.REQUEST_ID_KEY, generateRandomHexString());
            }


        } catch (final SOAPException e) {
            traceeLogger.error("TraceeServerHandler - Error during precessing of inbound soap header");
        }
    }

    protected final void handleOutbound(SOAPMessageContext context) {
        try {
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
                    context.setMessage(msg);
                } catch (final SOAPException e) {
                    traceeLogger.error("TraceeServerHandler : Exception "
                            + "occurred during processing of outbound message.", e);
                }


            }

        } finally {
            // must reset tracee context
            getTraceeBackend().clear();
        }
    }

    @Override
    public final boolean handleFault(SOAPMessageContext context) {
        this.handleOutbound(context);
        return true;
    }

    @Override
    public final Set<QName> getHeaders() {
        HashSet<QName> set = new HashSet<QName>();
        set.add(TraceeWsHandlerConstants.TRACEE_SOAP_HEADER_QNAME);
        return set;
    }
}
