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

	public static final String PROFILE_HIDE_INBOUND = "HideInbound";
	public static final String PROFILE_HIDE_OUTBOUND = "HideOutbound";

	public static final String TRACEE_SOAP_HEADER_CONTEXT_URL = "https://github.com/tracee/tracee";
	public static final String TRACEE_SOAP_HEADER_TAG_NAME = "TPIC";
	public static final QName TRACEE_SOAP_HEADER_QNAME = new QName(
			TRACEE_SOAP_HEADER_CONTEXT_URL,
			TRACEE_SOAP_HEADER_TAG_NAME);

}
