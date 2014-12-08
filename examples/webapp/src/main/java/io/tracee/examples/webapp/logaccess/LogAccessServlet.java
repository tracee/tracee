package io.tracee.examples.webapp.logaccess;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Log Servlet that provides access to log statements.
 */
@WebServlet(urlPatterns = "/viewLog")
public class LogAccessServlet extends HttpServlet {

	private final static LogMessageProvider logGrabberThread = new LogMessageProvider();

	@Override
	public void init() throws ServletException {

		// start thread
		logGrabberThread.start();

	}

	@Override
	public void destroy() {

		if (logGrabberThread.isAlive()) {
			// kill thread
			logGrabberThread.interrupt();
		}

	}

	@Override
	protected void doGet(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException, IOException {

		String requestOrSessionId = httpServletRequest.getParameter("id");
		ServletOutputStream sos = httpServletResponse.getOutputStream();
		if (requestOrSessionId != null) {
			List<LogMessage> logMessages = logGrabberThread.getLogMessages(requestOrSessionId);

			if (logMessages.size() > 0) {
				for (LogMessage logMessage : logMessages) {
					httpServletResponse.getOutputStream().println(logMessage.getLogMessage());
				}
			} else {
				sos.println("No log messages found - please try again in a few seconds");
			}
		} else {
			sos.println(" ");
		}

		// flush
		sos.flush();

	}
}
