package io.tracee.cxf.interceptor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement(name = "soapHeaderMap")
@XmlAccessorType(XmlAccessType.FIELD)
public class SoapHeaderMap {

	private Map<String, String> dataMap = new HashMap<String, String>();

	public SoapHeaderMap() {

	}

	public SoapHeaderMap(Map<String, String> filteredParams) {
		dataMap.putAll(filteredParams);
	}

	public Map<String, String> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, String> dataMap) {
		this.dataMap = dataMap;
	}
}
