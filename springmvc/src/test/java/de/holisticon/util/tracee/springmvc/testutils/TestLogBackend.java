package de.holisticon.util.tracee.springmvc.testutils;

import de.holisticon.util.tracee.TraceeLogger;
import de.holisticon.util.tracee.TraceeLoggerFactory;

/**
 * @author Sven Bunge, Holisticon AG
 */
public class TestLogBackend implements TraceeLoggerFactory {
	@Override
	public TraceeLogger getLogger(Class<?> clazz) {
		return new TraceeTestLogger();
	}

	public static class TraceeTestLogger implements TraceeLogger {
		@Override
		public void debug(Object message) {
			System.out.println(message);
		}

		@Override
		public void debug(Object message, Throwable t) {
			System.out.println(message);
			t.printStackTrace();
		}

		@Override
		public void error(Object message) {
			System.out.println(message);
		}

		@Override
		public void error(Object message, Throwable t) {
			System.out.println(message);
			t.printStackTrace();
		}

		@Override
		public void info(Object message) {
			System.out.println(message);
		}

		@Override
		public void info(Object message, Throwable t) {
			System.out.println(message);
			t.printStackTrace();
		}

		@Override
		public void warn(Object message) {
			System.out.println(message);
		}

		@Override
		public void warn(Object message, Throwable t) {
			System.out.println(message);
			t.printStackTrace();
		}
	}

}
