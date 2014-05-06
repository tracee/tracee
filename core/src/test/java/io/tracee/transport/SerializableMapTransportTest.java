package io.tracee.transport;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SerializableMapTransportTest {

	private SerializableMapTransport transport = new SerializableMapTransport();

	@Test
	public void parseDoNothingToGivenParameter() {
		final Map<String, String> testMap = Collections.unmodifiableMap(Collections.<String, String>emptyMap());

		assertThat(transport.parse(testMap), equalTo(testMap));
	}

	@Test
	public void serializableMapIsNotWrappedInRender() {
		// unmodifiableMap and the empty map is serializable
		final Map<String, String> testMap = Collections.unmodifiableMap(Collections.<String, String>emptyMap());

		assertThat(transport.render(testMap), equalTo(testMap));
	}

	@Test
	public void notSerializableMapIsSerializableAfterRender() throws Exception {
		final AbstractMap<String, String> notSerializableMap = new AbstractMap<String, String>() {
			@Override
			public Set<Entry<String, String>> entrySet() {
				return Collections.emptySet();
			}
		};

		final Map<String, String> assumedSerializableMap = transport.render(notSerializableMap);
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		new ObjectOutputStream(outputStream).writeObject(assumedSerializableMap);
		assertThat(outputStream.toByteArray().length, is(not(0)));
	}
}
