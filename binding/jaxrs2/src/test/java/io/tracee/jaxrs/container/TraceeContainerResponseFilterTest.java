package io.tracee.jaxrs.container;

import io.tracee.NoopTraceeLoggerFactory;
import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.transport.HttpHeaderTransport;
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
import static org.mockito.Mockito.when;

public class TraceeContainerResponseFilterTest {

    private final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
    private final TraceeContainerResponseFilter unit = new TraceeContainerResponseFilter(backend, new HttpHeaderTransport(new NoopTraceeLoggerFactory()));
    private final ContainerResponseContext responseContext = Mockito.mock(ContainerResponseContext.class);
    private final MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();

    @Before
    public void setUp() {
        when(responseContext.getHeaders()).thenReturn(headers);
        backend.clear();
    }

    @Test
    public void testFilterWritesContextToResponse() throws IOException {
        backend.put("foo", "ba r");
        unit.filter(null, responseContext);
        assertThat((String) responseContext.getHeaders().getFirst(TraceeConstants.HTTP_HEADER_NAME),
                equalTo("foo=ba+r"));
    }

    @Test
    public void testFilterCleansUpBackend() throws IOException {
        backend.put("random", "stuff");
        unit.filter(null, responseContext);
        assertTrue("backend is empty", backend.isEmpty());
    }
}
