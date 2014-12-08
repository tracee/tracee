package io.tracee.examples.webapp.logaccess;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

/**
 * @Test class for {@link io.tracee.examples.webapp.logaccess.LogMessageProvider}.
 */
public class LogMessageProviderTest {

	private LogMessageProvider logMessageProvider;

	@Before
	public void init() {
		logMessageProvider = new LogMessageProvider();
	}

	@Test
	public void should_detect_log_statement_beginning() {

		final String givenLogMessage = "01:31:27.799 [http-bio-9080-exec-10] INFO  i.t.e.webapp.TestWebappController - [0ba530f0110cb0890da7c7430460e051|XCF5H09NU7Z6HI5NQ1O1Q2D4HWSDD5H9] - summarize";
		boolean result = logMessageProvider.isLogMessageStart(givenLogMessage);
		MatcherAssert.assertThat(result, Matchers.is(true));

	}

	@Test
	public void should_return_false_for_no_log_statement_beginning() {

		final String givenLogMessage = "isadsahoudb  adsaoidsaodn  diuadoiasods  iasdoiuhashdaoisd";
		boolean result = logMessageProvider.isLogMessageStart(givenLogMessage);
		MatcherAssert.assertThat(result, Matchers.is(false));

	}

	@Test
	public void should_extract_timestamp() {

		final String givenTimestampStr = "01:31:27.799";
		long result = logMessageProvider.getTimestamp(givenTimestampStr);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(result);


		MatcherAssert.assertThat(calendar.get(Calendar.HOUR_OF_DAY), Matchers.is(1));
		MatcherAssert.assertThat(calendar.get(Calendar.MINUTE), Matchers.is(31));
		MatcherAssert.assertThat(calendar.get(Calendar.SECOND), Matchers.is(27));
		MatcherAssert.assertThat(calendar.get(Calendar.MILLISECOND), Matchers.is(799));

	}

	@Test(expected = IllegalArgumentException.class)
	public void should_throw_exception_for_invalid_timestamp_string() {

		final String givenTimestampStr = "01:31:27.7A9";
		logMessageProvider.getTimestamp(givenTimestampStr);

	}



}
