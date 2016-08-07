package io.tracee.configuration;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.Clock;
import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkHistoryChart;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;
import com.carrotsearch.junitbenchmarks.annotation.LabelType;
import io.tracee.TraceeConstants;
import org.junit.*;
import org.junit.rules.TestRule;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

@AxisRange(min = 0, max = 5)
@BenchmarkMethodChart(filePrefix = "benchmark-lists")
@BenchmarkHistoryChart(labelWith = LabelType.CUSTOM_KEY, maxRuns = 20)
public class PropertiesBasedTraceeFilterConfigurationBenchmarkTest {

	@Rule
	public TestRule benchmarkRun = new BenchmarkRule();

	public static final TraceeFilterConfiguration.Channel CHANNEL = TraceeFilterConfiguration.Channel.IncomingRequest;
	private PropertiesBasedTraceeFilterConfiguration unit;

	private Map<String, String> propertyMap;

	@Before
	public void before() throws IOException {
		final Properties traceeDefaultFileProperties = new TraceePropertiesFileLoader().loadTraceeProperties(TraceePropertiesFileLoader.TRACEE_DEFAULT_PROPERTIES_FILE);
		final Properties traceeFileProperties = new TraceePropertiesFileLoader().loadTraceeProperties(TraceePropertiesFileLoader.TRACEE_PROPERTIES_FILE);
		final PropertyChain propertyChain = PropertyChain.build(System.getProperties(), traceeFileProperties, traceeDefaultFileProperties);

		unit = new PropertiesBasedTraceeFilterConfiguration(propertyChain);
		generateTestPropertyMap();
		System.clearProperty(PropertiesBasedTraceeFilterConfiguration.TRACEE_DEFAULT_PROFILE_PREFIX + CHANNEL);
	}

	private void generateTestPropertyMap() {
		propertyMap = new HashMap<>();
		propertyMap.put(TraceeConstants.INVOCATION_ID_KEY, "reqID");
		propertyMap.put(TraceeConstants.SESSION_ID_KEY, "sessID");
		propertyMap.put("aaa", "AAA");
		propertyMap.put("bbb", "BBB");
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1)
	@Test
	@Ignore
	public void shouldPermitAll() {

		for (int i = 0; i < 2000000; i++) {
			final Map<String, String> filteredProperties = unit.DEFAULT.filterDeniedParams(propertyMap, CHANNEL);
			assertThat(filteredProperties.size(), is(4));
		}
	}

	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 1, clock = Clock.CPU_TIME )
	@Test
	@Ignore
	public void shouldProcessParam() {

		for (int i = 0; i < 2000000; i++) {
			assertTrue(unit.DEFAULT.shouldProcessParam("foo", CHANNEL));
		}
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1)
	@Test
	@Ignore
	public void shouldAllowTraceePrefixedParameters() {
		System.setProperty(PropertiesBasedTraceeFilterConfiguration.TRACEE_DEFAULT_PROFILE_PREFIX + CHANNEL, "tracee.*");

		for (int i = 0; i < 2000000; i++) {
			final Map<String, String> filteredProperties = unit.DEFAULT.filterDeniedParams(propertyMap, CHANNEL);
			assertThat(filteredProperties.size(), is(2));
		}
	}
}
