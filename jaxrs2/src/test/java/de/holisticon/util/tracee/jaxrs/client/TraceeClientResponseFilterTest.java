package de.holisticon.util.tracee.jaxrs.client;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
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

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeClientResponseFilterTest {

    private final TraceeBackend backend = Tracee.getBackend();
    private final TraceeClientResponseFilter unit = new TraceeClientResponseFilter();
    private final ClientResponseContext clientResponseContext = mock(ClientResponseContext.class);
    private final MultivaluedMap<String,String> headers = new MultivaluedHashMap<String, String>();

    @Before
    public void setUp() {
        when(clientResponseContext.getHeaders()).thenReturn(headers);
        backend.clear();
    }

    @Test
    public void testFilter() throws IOException {
        headers.putSingle(TraceeConstants.HTTP_HEADER_NAME, "{\"foo\":\"bar\"}");
        unit.filter(null, clientResponseContext);
        assertThat(backend.get("foo"), equalTo("bar"));
    }

}
