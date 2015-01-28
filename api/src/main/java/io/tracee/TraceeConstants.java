package io.tracee;

import javax.xml.namespace.QName;

public interface TraceeConstants {

	// TPIC = TracEE propagated invocation context
    String HTTP_HEADER_NAME = "TPIC";
    String JMS_HEADER_NAME = "TPIC";

    String SESSION_ID_KEY = "traceeSessionId";
    String REQUEST_ID_KEY = "traceeRequestId";

	String SOAP_HEADER_NAME = "tpic";
	String SOAP_HEADER_NAMESPACE = "http://tracee.io/tpic/1.0";
	QName SOAP_HEADER_QNAME = new QName(SOAP_HEADER_NAMESPACE,SOAP_HEADER_NAME);
}
