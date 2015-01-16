package io.tracee.binding.jaxrs2;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class TraceeClientRequestFilterTest {

    private final TraceeBackend traceeBackend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
    private final TraceeClientRequestFilter unit = new TraceeClientRequestFilter(traceeBackend);
    private final ClientRequestContext clientRequestContext = Mockito.mock(ClientRequestContext.class);

    @Before
    public void setUp() {
        final MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        when(clientRequestContext.getHeaders()).thenReturn(headers);
    }

    @Test
    public void testFilterWritesBackendToRequestContextHeaders() throws IOException {
        traceeBackend.put("foo", "bar");
        unit.filter(clientRequestContext);
        assertThat((String) clientRequestContext.getHeaders().getFirst(TraceeConstants.HTTP_HEADER_NAME),
                containsString("foo=bar"));
    }

    @Test
    public void doNotCreateRequestIdOnOutgoingRequest() throws IOException {
        unit.filter(clientRequestContext);
        assertThat(traceeBackend.get(TraceeConstants.REQUEST_ID_KEY), isEmptyOrNullString());
        assertThat(clientRequestContext.getHeaders().getFirst(TraceeConstants.HTTP_HEADER_NAME),
                is(nullValue()));
    }

    @Test
    public void testFilterKeepsExistingRequestId() throws IOException {
        traceeBackend.put(TraceeConstants.REQUEST_ID_KEY, "foo");
        unit.filter(clientRequestContext);
        assertThat(traceeBackend.get(TraceeConstants.REQUEST_ID_KEY), equalTo("foo"));
        assertThat((String) clientRequestContext.getHeaders().getFirst(TraceeConstants.HTTP_HEADER_NAME),
                containsString(TraceeConstants.REQUEST_ID_KEY + "=foo"));
    }
}
