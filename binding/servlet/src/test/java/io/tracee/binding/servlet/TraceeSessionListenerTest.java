package io.tracee.binding.servlet;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.FieldAccessUtil;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import static io.tracee.TraceeConstants.SESSION_ID_KEY;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;

public class TraceeSessionListenerTest {

	private final TraceeBackend backend = mock(TraceeBackend.class);
	private final HttpSession session = mock(HttpSession.class);
	private final TraceeFilterConfiguration configuration = mock(TraceeFilterConfiguration.class);
	private final TraceeSessionListener unit = new TraceeSessionListener(backend, configuration);

	@Before
	public void setUpMocks() {
		when(backend.getConfiguration()).thenReturn(configuration);
		when(session.getId()).thenReturn("A_SESSION_ID");
	}

	@Test
	public void testWriteSessionIdToContextOnCreateIfConfigured() {
		when(configuration.shouldGenerateSessionId()).thenReturn(true);
		unit.sessionCreated(new HttpSessionEvent(session));
		verify(backend, atLeastOnce()).put(eq(SESSION_ID_KEY), anyString());
	}

	@Test
	public void testDontWriteSessionIdToContextOnCreateIfNotConfigured() {
		when(configuration.shouldGenerateSessionId()).thenReturn(false);
		unit.sessionCreated(new HttpSessionEvent(session));
		verify(backend, never()).put(eq(SESSION_ID_KEY), anyString());
	}

	@Test
	public void cleanupSessionOnDestroy() {
		unit.sessionDestroyed(mock(HttpSessionEvent.class));
		verify(backend).remove(SESSION_ID_KEY);
	}

	@Test
	public void defaultConstructorUsesTraceeBackend() {
		final TraceeSessionListener listener = new TraceeSessionListener();
		MatcherAssert.assertThat((TraceeBackend) FieldAccessUtil.getFieldVal(listener, "backend"), is(Tracee.getBackend()));
	}
}
