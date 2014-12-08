package io.tracee.examples.webapp.logaccess;

import java.util.Calendar;

/**
 * Class that stores a single log message.
 */
public class LogMessage {

	private final long timestamp;
	private final String requestId;
	private final String sessionId;
	private final StringBuilder logMessage;

	/**
	 * Main constructor.
	 *
	 * @param timestamp  the timestamp of the log statement
	 * @param requestId  the request id, may be empty but must not be null
	 * @param sessionId  the session id, may be empty but must not be null
	 * @param logMessage the first log message line
	 */
	public LogMessage(final long timestamp, final String requestId, final String sessionId, final String logMessage) {
		this.timestamp = timestamp;
		this.requestId = requestId;
		this.sessionId = sessionId;
		this.logMessage = new StringBuilder(logMessage);
	}

	/**
	 * Appends a line to a log message.
	 *
	 * @param logMessage
	 */
	public void addLineToLogMessage(final String logMessage) {
		this.logMessage.append("\n").append(logMessage);
	}

	public String getRequestId() {
		return requestId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getLogMessage() {
		return logMessage.toString();
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getTimestampAsString() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(this.timestamp);
		return calendar.toString();
	}

}
