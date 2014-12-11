package io.tracee.transport.jaxb;

import io.tracee.TraceeConstants;

import javax.xml.bind.annotation.*;
import java.util.*;

@XmlRootElement(name = TraceeConstants.SOAP_HEADER_NAME)
@XmlAccessorType(XmlAccessType.NONE)
public class TpicMap {

	@XmlElement(name = "entry", nillable = false)
	public List<Entry> entries;

	@SuppressWarnings("UnusedDeclaration")
	private TpicMap() { entries = null; }


	public TpicMap(List<Entry> entries) {
		this.entries = entries;
	}

	public static TpicMap wrap(Map<String, String> map) {
		final List<Entry> values = new ArrayList<Entry>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			values.add(new Entry(entry.getKey(), entry.getValue()));
		}
		return new TpicMap(values);
	}

	public Map<String, String> unwrapValues() {
		if (entries == null) {
			return Collections.emptyMap();
		}
		final Map<String, String> map = new HashMap<String,String>();
		for (Entry value : this.entries) {
			map.put(value.key, value.value);
		}
		return map;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TpicMap tpicMap = (TpicMap) o;

		if (!entries.equals(tpicMap.entries)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return entries.hashCode();
	}

	public static class Entry {

		@XmlAttribute(name = "key", required = true)
		public final String key;
		@XmlValue
		public final String value;

		public Entry(String key, String value) {
			this.key = key;
			this.value = value;
		}

		@SuppressWarnings(value = "unused")
		protected Entry() {
			this.key = null;
			this.value = null;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Entry entry = (Entry) o;

			if (!key.equals(entry.key)) return false;
			if (!value.equals(entry.value)) return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = key.hashCode();
			result = 31 * result + value.hashCode();
			return result;
		}
	}
}
