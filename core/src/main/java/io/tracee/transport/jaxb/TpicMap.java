package io.tracee.transport.jaxb;

import io.tracee.TraceeConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Map;

@XmlRootElement(name = TraceeConstants.TRACEE_SOAP_HEADER_TAG_NAME, namespace = TraceeConstants.TRACEE_SOAP_HEADER_CONTEXT_URL)
@XmlAccessorType(XmlAccessType.FIELD)
public class TpicMap {

	@XmlElement(name = "values")
	@XmlJavaTypeAdapter(TpicHeaderValueAdapter.class)
	public Map<String, String> tpicValues;

	private TpicMap() {
	}

	private TpicMap(Map<String, String> tpicValue) {
		this.tpicValues = tpicValue;
	}

	public static TpicMap wrap(Map<String, String> map) {
		return new TpicMap(map);
	}

	public Map<String, String> unwrap() {
		return tpicValues;
	}
}
