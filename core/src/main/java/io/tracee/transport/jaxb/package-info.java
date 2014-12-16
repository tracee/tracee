@XmlSchema(
		namespace = TraceeConstants.SOAP_HEADER_NAMESPACE, elementFormDefault = XmlNsForm.QUALIFIED,
		xmlns = @XmlNs(namespaceURI = TraceeConstants.SOAP_HEADER_NAMESPACE, prefix = ""))
package io.tracee.transport.jaxb;

import io.tracee.TraceeConstants;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
