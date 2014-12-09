package io.tracee.transport.jaxb;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TpicHeaderValueAdapter extends XmlAdapter<TpicHeaderValueAdapter.TpicValueList, Map<String, String>> {

	@Override
	public Map<String, String> unmarshal(TpicValueList valuesList) throws Exception {
		if (valuesList == null) {
			return null;
		}
		final Map<String, String> map = new HashMap<String, String>();
		for (TpicValue value : valuesList.values) {
			map.put(value.key, value.value);
		}
		return map;
	}

	@Override
	public TpicValueList marshal(Map<String, String> values) throws Exception {
		if (values == null) {
			return null;
		}
		TpicValueList valueList = new TpicValueList();
		for (Map.Entry<String, String> entry : values.entrySet()) {
			valueList.values.add(new TpicValue(entry.getKey(), entry.getValue()));
		}

		return valueList;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class TpicValueList {
		@XmlElement(name = "value")
		final List<TpicValue> values = new ArrayList<TpicValue>();
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class TpicValue {
		@XmlAttribute(name = "key")
		private String key;

		@XmlValue
		private String value;

		private TpicValue() {
		}

		public TpicValue(String key, String value) {
			this.key = key;
			this.value = value;
		}
	}
}
