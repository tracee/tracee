package io.tracee.jaxrs.client;

import io.tracee.NoopTraceeLoggerFactory;
import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.transport.HttpHeaderTransport;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TraceeClientResponseFilterTest {

    private final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
    private final TraceeClientResponseFilter unit = new TraceeClientResponseFilter(backend, new HttpHeaderTransport(new NoopTraceeLoggerFactory()));
    private final ClientResponseContext clientResponseContext = mock(ClientResponseContext.class);
    private final MultivaluedMap<String, String> headers = new MultivaluedHashMap<String, String>();

    @Before
    public void setUp() {
        when(clientResponseContext.getHeaders()).thenReturn(headers);
        backend.clear();
    }

    @Test
    public void testFilterParsesContextFromHeaderToBackend() throws IOException {
        headers.putSingle(TraceeConstants.HTTP_HEADER_NAME, "foo=bar");
        unit.filter(null, clientResponseContext);
        assertThat(backend.get("foo"), equalTo("bar"));
    }

}
