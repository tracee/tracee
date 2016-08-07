@XmlSchema(
	namespace = HelloWorldTestService.NAMESPACE, elementFormDefault = XmlNsForm.QUALIFIED,
	xmlns = @XmlNs(namespaceURI = HelloWorldTestService.NAMESPACE, prefix = ""))
package io.tracee.binding.cxf.testSoapService;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
