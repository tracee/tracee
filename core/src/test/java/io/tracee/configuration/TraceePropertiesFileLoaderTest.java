package io.tracee.configuration;

import io.tracee.MDCLikeTraceeBackend;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceePropertiesFileLoaderTest {

	private final TraceePropertiesFileLoader unit = new TraceePropertiesFileLoader();

	@Test
	public void testLoadDefaultProperties() throws IOException {
		final Properties properties = unit.loadTraceeProperties(MDCLikeTraceeBackend.TRACEE_DEFAULT_PROPERTIES_FILE);
		assertThat("property tracee.default.IncomingRequest",properties.getProperty("tracee.default.IncomingRequest"), not(isEmptyOrNullString()));
		assertThat("property tracee.default.OutgoingResponse",properties.getProperty("tracee.default.OutgoingResponse"), not(isEmptyOrNullString()));
		assertThat("property tracee.default.OutgoingRequest",properties.getProperty("tracee.default.OutgoingRequest"), not(isEmptyOrNullString()));
		assertThat("property tracee.default.IncomingResponse",properties.getProperty("tracee.default.IncomingResponse"), not(isEmptyOrNullString()));
		assertThat("property tracee.default.AsyncDispatch",properties.getProperty("tracee.default.AsyncDispatch"), not(isEmptyOrNullString()));
		assertThat("property tracee.default.AsyncProcess",properties.getProperty("tracee.default.AsyncProcess"), not(isEmptyOrNullString()));
		assertThat("property tracee.default.requestIdLength",properties.getProperty("tracee.default.requestIdLength"), not(isEmptyOrNullString()));
		assertThat("property tracee.default.sessionIdLength",properties.getProperty("tracee.default.sessionIdLength"), not(isEmptyOrNullString()));
	}

}
