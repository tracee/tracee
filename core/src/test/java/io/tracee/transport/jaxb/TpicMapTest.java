package io.tracee.transport.jaxb;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class TpicMapTest {

	@Test
	public void tpicMapShouldImplementEqualsCorrect() {
		EqualsVerifier.forClass(TpicMap.class).verify();
	}

	@Test
	public void tpicMapEntryShouldImplementEqualsCorrect() {
		EqualsVerifier.forClass(TpicMap.Entry.class).verify();
	}
}
