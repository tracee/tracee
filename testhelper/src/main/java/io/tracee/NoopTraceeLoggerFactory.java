package io.tracee;

public class NoopTraceeLoggerFactory implements TraceeLoggerFactory {


	public static final NoopTraceeLoggerFactory INSTANCE = new NoopTraceeLoggerFactory();

	@Override
	public final TraceeLogger getLogger(Class<?> clazz) {
		return new TraceeLogger() {
			@Override
			public void debug(String message) {

			}

			@Override
			public void debug(String message, Throwable t) {

			}

			@Override
			public boolean isDebugEnabled() {
				return false;
			}

			@Override
			public void error(String message) {

			}

			@Override
			public void error(String message, Throwable t) {

			}

			@Override
			public boolean isErrorEnabled() {
				return false;
			}

			@Override
			public void info(String message) {

			}

			@Override
			public void info(String message, Throwable t) {

			}

			@Override
			public boolean isInfoEnabled() {
				return false;
			}

			@Override
			public void warn(String message) {

			}

			@Override
			public void warn(String message, Throwable t) {

			}

			@Override
			public boolean isWarnEnabled() {
				return false;
			}
		};
	}
}
