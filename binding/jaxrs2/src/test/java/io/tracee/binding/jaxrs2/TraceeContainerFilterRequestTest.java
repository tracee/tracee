package io.tracee.binding.jaxrs2;

import io.tracee.Tracee;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.FieldAccessUtil;
import io.tracee.testhelper.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class TraceeContainerFilterRequestTest {

    private final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
    private final TraceeContainerFilter unit = new TraceeContainerFilter(backend);
    private final ContainerRequestContext requestContext = Mockito.mock(ContainerRequestContext.class);
    private final MultivaluedMap<String, String> headers = new MultivaluedHashMap<String, String>();

    @Before
    public void setUp() {
        when(requestContext.getHeaders()).thenReturn(headers);
        backend.clear();
    }

    @Test
    public void testFilterParsesContextFromHeaderToBackend() throws IOException {
        headers.putSingle(TraceeConstants.TPIC_HEADER, "foo=bar");
        unit.filter(requestContext);
        assertThat(backend.get("foo"), equalTo("bar"));
    }

    @Test
    public void testFilterParsesExistingInvocationId() throws IOException {
        headers.putSingle(TraceeConstants.TPIC_HEADER, TraceeConstants.INVOCATION_ID_KEY + "=foo");
        unit.filter(requestContext);
        assertThat(backend.get(TraceeConstants.INVOCATION_ID_KEY), equalTo("foo"));
    }

    @Test
    public void testFilterCreatesInvocationIdIfNotInHeaders() throws IOException {
        unit.filter(requestContext);
        assertThat(backend.get(TraceeConstants.INVOCATION_ID_KEY), not(isEmptyOrNullString()));
    }

	@Test
	public void defaultConstructorUsesDefaultProfile() {
		final TraceeContainerFilter containerFilter = new TraceeContainerFilter();
		MatcherAssert.assertThat((TraceeBackend) FieldAccessUtil.getFieldVal(containerFilter, "backend"), is(Tracee.getBackend()));
	}
}
