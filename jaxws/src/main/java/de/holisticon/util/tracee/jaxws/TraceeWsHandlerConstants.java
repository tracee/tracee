package de.holisticon.util.tracee.jaxws;

import javax.xml.namespace.QName;

/**
 * Created by Tobias Gindler, holisticon AG on 06.12.13.
 */
public final class TraceeWsHandlerConstants {

    private TraceeWsHandlerConstants() {

    }

    public static final String TRACEE_SOAP_HEADER_CONTEXT_URL = "https://github.com/holisticon/tracee";
    public static final String TRACEE_SOAP_HEADER_TAG_NAME = "tracee";
    public static final QName TRACEE_SOAP_HEADER_QNAME = new QName(
            TRACEE_SOAP_HEADER_CONTEXT_URL,
            TRACEE_SOAP_HEADER_TAG_NAME);


}
