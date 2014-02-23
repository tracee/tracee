package de.holisticon.util.tracee.configuration;

import de.holisticon.util.tracee.MDCLikeTraceeBackend;
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
		assertThat("property tracee.IncomingRequest",properties.getProperty("tracee.IncomingRequest"), not(isEmptyOrNullString()));
		assertThat("property tracee.OutgoingResponse",properties.getProperty("tracee.OutgoingResponse"), not(isEmptyOrNullString()));
		assertThat("property tracee.OutgoingRequest",properties.getProperty("tracee.OutgoingRequest"), not(isEmptyOrNullString()));
		assertThat("property tracee.IncomingResponse",properties.getProperty("tracee.IncomingResponse"), not(isEmptyOrNullString()));
		assertThat("property tracee.requestIdLength",properties.getProperty("tracee.requestIdLength"), not(isEmptyOrNullString()));
		assertThat("property tracee.sessionIdLength",properties.getProperty("tracee.sessionIdLength"), not(isEmptyOrNullString()));
	}

}
