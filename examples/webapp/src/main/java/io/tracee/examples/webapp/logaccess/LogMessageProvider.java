package io.tracee.examples.webapp.logaccess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Log Message provider
 */
public class LogMessageProvider extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogMessageProvider.class);

	private final static String LOG_FILE_SYSTEM_PROPERTY_NAME = "io.tracee.example.webapp.logfile";
	private final static Pattern LOG_MESSAGE_PATTERN = Pattern.compile("(\\d{2}:\\d{2}:\\d{2}\\.\\d{1,3})\\s\\[.*?\\] (.*?) .* - \\[(.*)\\|(.*)\\] - (.*)");
	private final static Pattern TIMESTAMP_PATTERN = Pattern.compile("(\\d{2}):(\\d{2}):(\\d{2})\\.(\\d{1,3})");
	private final static long SEARCH_WINDOW_IN_BYTES = 10000000;
	private final String logFileName;

	private List<LogMessage> logMessageBuffer = new ArrayList<LogMessage>();

	public LogMessageProvider() {
		String tmpLogFileName = System.getProperty(LOG_FILE_SYSTEM_PROPERTY_NAME);
		if (tmpLogFileName == null) {
			LOGGER.error("System property '" + LOG_FILE_SYSTEM_PROPERTY_NAME + "' must be set to define location of log file . ");
		} else if (!(new File(tmpLogFileName).exists())) {
			tmpLogFileName = null;
			LOGGER.error("Log file '" + tmpLogFileName + "' defined via system property '" + LOG_FILE_SYSTEM_PROPERTY_NAME + "' must be existent . ");
		}

		logFileName = tmpLogFileName;


	}

	@Override
	public void run() {
		if (logFileName != null) {
			try {
				while (this.isInterrupted() == false) {

					updateLogMessageBuffer();

					Thread.sleep(5000);

				}
			} catch (InterruptedException e) {
				LOGGER.error("Thread is interrupted");
			}
		}

	}

	/**
	 * Gets all log messages with matching request or session id.
	 *
	 * @param requestOrSessionId the request or session id to look for
	 * @return A list containing all matching log messages
	 */
	public List<LogMessage> getLogMessages(final String requestOrSessionId) {

		List<LogMessage> localBuffer = this.logMessageBuffer;
		List<LogMessage> filteredBuffer = new ArrayList<LogMessage>();

		if (requestOrSessionId == null || requestOrSessionId.isEmpty()) {
			return filteredBuffer;
		}

		for (LogMessage logMessage : localBuffer) {
			if (requestOrSessionId.equals(logMessage.getRequestId()) || requestOrSessionId.equals(logMessage.getSessionId())) {
				filteredBuffer.add(logMessage);
			}
		}

		return filteredBuffer;

	}

	void updateLogMessageBuffer() {
		BufferedReader bufferedReader = null;
		final List<LogMessage> localLogMessages = new ArrayList<LogMessage>();

		try {
			File file = new File(logFileName);

			long startIndex = file.length() - SEARCH_WINDOW_IN_BYTES;

			bufferedReader = new BufferedReader(new FileReader(file));

			if (startIndex > 0) {
				bufferedReader.skip(startIndex);
			}

			String line = bufferedReader.readLine();

			// move forward to first log message
			while (line != null && !this.isLogMessageStart(line)) {
				line = bufferedReader.readLine();
			}
			LogMessage lastLogMessage = extractLogMessage(line);
			localLogMessages.add(lastLogMessage);

			while (line != null) {

				if (!this.isLogMessageStart(line)) {
					lastLogMessage.addLineToLogMessage(line);
				} else {
					lastLogMessage = extractLogMessage(line);
					localLogMessages.add(lastLogMessage);
				}

				// get next line
				line = bufferedReader.readLine();
			}

			this.logMessageBuffer = localLogMessages;

		} catch (Exception e) {
			LOGGER.error("Log file can't be read", e);
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					LOGGER.error("Log file can't be closed", e);
				}
			}
		}

	}

	boolean isLogMessageStart(final String logMessageLine) {

		Matcher matcher = LOG_MESSAGE_PATTERN.matcher(logMessageLine);
		return matcher.matches();

	}

	LogMessage extractLogMessage(final String logMessage) {

		Matcher matcher = LOG_MESSAGE_PATTERN.matcher(logMessage);
		if (matcher.matches()) {

			final long timestamp = getTimestamp(matcher.group(1));
			final String sessionId = matcher.group(3);
			final String requestId = matcher.group(4);

			return new LogMessage(timestamp, requestId, sessionId, logMessage);
		} else {
			throw new IllegalArgumentException("Passed log message is not a log message");
		}

	}

	/**
	 * Gets the Timestamp as milliseconds.
	 *
	 * @param timestamp the string to be converted into a timestamp.
	 * @return
	 */
	long getTimestamp(final String timestamp) {

		Matcher matcher = TIMESTAMP_PATTERN.matcher(timestamp);
		if (matcher.matches()) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(matcher.group(1)));
			calendar.set(Calendar.MINUTE, Integer.valueOf(matcher.group(2)));
			calendar.set(Calendar.SECOND, Integer.valueOf(matcher.group(3)));
			calendar.set(Calendar.MILLISECOND, Integer.valueOf(matcher.group(4)));

			return calendar.getTimeInMillis();

		} else {
			throw new IllegalArgumentException("Passed timestamp string has wrong format");
		}


	}


}
