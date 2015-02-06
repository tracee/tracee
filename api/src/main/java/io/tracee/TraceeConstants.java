package io.tracee;

import javax.xml.namespace.QName;

public final class TraceeConstants {

	// TPIC = TracEE propagated invocation context
	public static final String TPIC_HEADER = "TPIC";

	public static final String SOAP_HEADER_NAMESPACE = "http://tracee.io/tpic/1.0";

	public static final QName SOAP_HEADER_QNAME = new QName(SOAP_HEADER_NAMESPACE, TPIC_HEADER);

	public static final String SESSION_ID_KEY = "TPIC.sessionId";
    public static final String INVOCATION_ID_KEY = "TPIC.invocationId";

	private TraceeConstants() {
	}
}
