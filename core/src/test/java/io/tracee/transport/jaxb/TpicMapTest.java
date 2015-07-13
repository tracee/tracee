package io.tracee.transport.jaxb;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class TpicMapTest {

	@Test
	public void tpicMapShouldImplementEqualsCorrect() {
		EqualsVerifier.forClass(TpicMap.class).verify();
	}

	@Test
	public void tpicMapEntryShouldImplementEqualsCorrect() {
		EqualsVerifier.forClass(TpicMap.Entry.class).verify();
	}

	@Test
	public void toStringShouldContainTheKey() {
		assertThat(new TpicMap.Entry("theKey", "null").toString(), containsString("theKey"));
	}

	@Test
	public void toStringShouldContainTheValue() {
		assertThat(new TpicMap.Entry("null", "theValue").toString(), containsString("theValue"));
	}
}
