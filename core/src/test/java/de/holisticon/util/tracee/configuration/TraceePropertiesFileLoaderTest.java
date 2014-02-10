package de.holisticon.util.tracee.configuration;

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
		final Properties properties = unit.loadTraceeProperties();
		assertThat("property tracee.IncomingRequest",properties.getProperty("tracee.IncomingRequest"), not(isEmptyOrNullString()));
		assertThat("property tracee.OutgoingResponse",properties.getProperty("tracee.OutgoingResponse"), not(isEmptyOrNullString()));
		assertThat("property tracee.OutgoingRequest",properties.getProperty("tracee.OutgoingRequest"), not(isEmptyOrNullString()));
		assertThat("property tracee.IncomingResponse",properties.getProperty("tracee.IncomingResponse"), not(isEmptyOrNullString()));
	}

}
