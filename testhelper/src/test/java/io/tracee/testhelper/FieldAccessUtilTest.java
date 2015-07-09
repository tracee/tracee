package io.tracee.testhelper;

import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FieldAccessUtilTest {

	@Test
	public void shouldAccessFields() {
		final SimpleTraceeBackend backend = new SimpleTraceeBackend(new PermitAllTraceeFilterConfiguration());
		backend.put("test", "testVal");
		@SuppressWarnings("unchecked")
		final Map<String, String> value = FieldAccessUtil.getFieldVal(backend, "backendValues");
		assertThat(value.get("test"), is("testVal"));
	}
}
