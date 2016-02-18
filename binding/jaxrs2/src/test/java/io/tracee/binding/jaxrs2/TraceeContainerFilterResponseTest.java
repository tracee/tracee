package io.tracee.binding.jaxrs2;

import io.tracee.testhelper.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class TraceeContainerFilterResponseTest {

    private final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
    private final TraceeContainerFilter unit = new TraceeContainerFilter(backend);
    private final ContainerResponseContext responseContext = Mockito.mock(ContainerResponseContext.class);
    private final MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();

    @Before
    public void setUp() {
        when(responseContext.getHeaders()).thenReturn(headers);
        backend.clear();
    }

    @Test
    public void testFilterWritesContextToResponse() throws IOException {
        backend.put("foo", "ba r");
        unit.filter(null, responseContext);
        assertThat((String) responseContext.getHeaders().getFirst(TraceeConstants.TPIC_HEADER),
                equalTo("foo=ba+r"));
    }

    @Test
    public void testFilterCleansUpBackend() throws IOException {
        backend.put("random", "stuff");
        unit.filter(null, responseContext);
		assertThat("backend has not been cleaned", backend.isEmpty(), is(true));
    }
}
