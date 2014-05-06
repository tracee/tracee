package io.tracee.jaxws;

import javax.xml.namespace.QName;

public final class TraceeWsHandlerConstants {

    private TraceeWsHandlerConstants() {

    }

    public static final String TRACEE_SOAP_HEADER_CONTEXT_URL = "https://github.com/holisticon/tracee";
    public static final String TRACEE_SOAP_HEADER_TAG_NAME = "tracee";
    public static final QName TRACEE_SOAP_HEADER_QNAME = new QName(
            TRACEE_SOAP_HEADER_CONTEXT_URL,
            TRACEE_SOAP_HEADER_TAG_NAME);

    public static final String TRACEE_HANDLER_CHAIN_URL =
            "/io/tracee/jaxws/TraceeHandlerChain.xml";



}
