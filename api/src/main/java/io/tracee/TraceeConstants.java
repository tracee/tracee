package io.tracee;

import javax.xml.namespace.QName;

public final class TraceeConstants {

    private TraceeConstants() {
    }

	// TPIC = TracEE propagated invocation context
    public static final String HTTP_HEADER_NAME = "TPIC";
    public static final String JMS_HEADER_NAME = "TPIC";

    public static final String SESSION_ID_KEY = "traceeSessionId";
    public static final String REQUEST_ID_KEY = "traceeRequestId";

	public static final String SOAP_HEADER_NAME = "tpic";
	public static final String SOAP_HEADER_NAMESPACE = "http://tracee.io/tpic/1.0";
	public static final QName SOAP_HEADER_QNAME = new QName(SOAP_HEADER_NAMESPACE,SOAP_HEADER_NAME);
}
