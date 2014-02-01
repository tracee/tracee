package de.holisticon.util.tracee.jaxrs.client;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;


/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeClientRequestFilterTest {

    private final TraceeBackend traceeBackend = Tracee.getBackend();
    private final TraceeClientRequestFilter unit = new TraceeClientRequestFilter();
    private ClientRequestContext clientRequestContext = Mockito.mock(ClientRequestContext.class);



    @Before
    public void setUp() {
        traceeBackend.clear();
        final MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        when(clientRequestContext.getHeaders()).thenReturn(headers);
    }

    @Test
    public void testFilter() throws IOException {
        traceeBackend.put("foo", "bar");
        unit.filter(clientRequestContext);
        assertThat((String)clientRequestContext.getHeaders().getFirst(TraceeConstants.HTTP_HEADER_NAME),
                Matchers.equalTo("{\"foo\":\"bar\"}"));
    }

}
