package de.holisticon.util.tracee.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;


/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class PropertyChainTest {

	private final Properties p1 = Mockito.mock(Properties.class);
	private final Properties p2 = Mockito.mock(Properties.class);

	@Before
	public void initMocks() {
		when(p1.getProperty("ONLY_IN_P1")).thenReturn("FROM_P1");
		when(p1.getProperty("IN_BOTH")).thenReturn("FROM_P1");
		when(p2.getProperty("ONLY_IN_P2")).thenReturn("FROM_P2");
		when(p2.getProperty("IN_BOTH")).thenReturn("FROM_P2");
	}

	@Test
	public void shouldReturnAlwaysNullsForEmptyPropertyChain() {
		final PropertyChain unit = new PropertyChain(Collections.<Properties>emptyList());
		assertThat(unit.getProperty("ANY KEY"), nullValue());
	}

	@Test
	public void testShouldWrapSingleProperties() throws Exception {
		final PropertyChain unit = new PropertyChain(Arrays.asList(p1));
		assertThat(unit.getProperty("ONLY_IN_P1"), equalTo("FROM_P1"));
	}

	@Test
	public void testShouldWrapMultipleProperties() throws Exception {
		final PropertyChain unit = new PropertyChain(Arrays.asList(p1, p2));
		assertThat(unit.getProperty("ONLY_IN_P1"), equalTo("FROM_P1"));
		assertThat(unit.getProperty("ONLY_IN_P2"), equalTo("FROM_P2"));
		assertThat(unit.getProperty("IN_BOTH"), equalTo("FROM_P1"));
	}
}
