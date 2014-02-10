package de.holisticon.util.tracee.jaxrs.container;

import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.TraceeLoggerFactory;
import de.holisticon.util.tracee.configuration.TraceeFilterConfiguration;
import de.holisticon.util.tracee.jaxrs.MockTraceeBackend;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeContainerResponseFilterTest {

	private final TraceeFilterConfiguration configuration = Mockito.mock(TraceeFilterConfiguration.class);
	private final TraceeLoggerFactory loggerFactory = Mockito.mock(TraceeLoggerFactory.class);
    private final TraceeBackend backend = new MockTraceeBackend(configuration, loggerFactory);
    private final TraceeContainerResponseFilter unit = new TraceeContainerResponseFilter(backend);
    private final ContainerResponseContext responseContext = Mockito.mock(ContainerResponseContext.class);
    private final MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();

    @Before
    public void setUp() {
		when(configuration.shouldProcessContext(any(TraceeFilterConfiguration.MessageType.class))).thenReturn(true);
		when(configuration.generatedRequestIdLength()).thenReturn(32);
        when(responseContext.getHeaders()).thenReturn(headers);
        backend.clear();
    }

    @Test
    public void testFilterWritesContextToResponse() throws IOException {
        backend.put("foo", "bar");
        unit.filter(null, responseContext);
        assertThat((String)responseContext.getHeaders().getFirst(TraceeConstants.HTTP_HEADER_NAME),
                equalTo("{\"foo\":\"bar\"}"));
    }

    @Test
    public void testFilterCleansUpBackend() throws IOException {
        backend.put("random","stuff");
        unit.filter(null, responseContext);
        assertTrue("backend is empty", backend.isEmpty());
    }
}