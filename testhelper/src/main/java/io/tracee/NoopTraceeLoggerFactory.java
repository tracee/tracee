package io.tracee;

public class NoopTraceeLoggerFactory implements TraceeLoggerFactory {


	public static final NoopTraceeLoggerFactory INSTANCE = new NoopTraceeLoggerFactory();

	@Override
	public final TraceeLogger getLogger(Class<?> clazz) {
		return new TraceeLogger() {
			@Override
			public void debug(Object message) {

			}

			@Override
			public void debug(Object message, Throwable t) {

			}

			@Override
			public boolean isDebugEnabled() {
				return false;
			}

			@Override
			public void error(Object message) {

			}

			@Override
			public void error(Object message, Throwable t) {

			}

			@Override
			public boolean isErrorEnabled() {
				return false;
			}

			@Override
			public void info(Object message) {

			}

			@Override
			public void info(Object message, Throwable t) {

			}

			@Override
			public boolean isInfoEnabled() {
				return false;
			}

			@Override
			public void warn(Object message) {

			}

			@Override
			public void warn(Object message, Throwable t) {

			}

			@Override
			public boolean isWarnEnabled() {
				return false;
			}
		};
	}
}
