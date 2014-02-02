package de.holisticon.util.tracee.jaxrs.itest;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.jaxrs.client.TraceeClientRequestFilter;
import de.holisticon.util.tracee.jaxrs.client.TraceeClientResponseFilter;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class AllFilterIT {

    private static final String ENDPOINT_URL = "http://127.0.0.1:4204/jaxrs2/";
    private HttpServer server;

    @Before
    public void setUp() throws Exception {
        Tracee.getBackend().clear();
        final ResourceConfig rc = new ResourceConfig().packages("de.holisticon.util.tracee.jaxrs");
        server = GrizzlyHttpServerFactory.createHttpServer(URI.create(ENDPOINT_URL), rc);
    }

    @After
    public void tearDown() throws ExecutionException, InterruptedException {
        server.shutdown().get();
    }

    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public static class DummyResource {
        @GET
        public Response resource() {

            if ("yes".equals(Tracee.getBackend().get("beforeRequest"))) {
                Tracee.getBackend().put("beforeRequest", "no");
                Tracee.getBackend().put("beforeResponse", "yes");
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

        }
    }

    @Test
    public void testRoundtrip() {

        final Client client = ClientBuilder.newClient().register(TraceeClientRequestFilter.class).register(TraceeClientResponseFilter.class);

        Tracee.getBackend().put("beforeRequest", "yes");
        final Response response = client.target(ENDPOINT_URL).request().get();
        assertThat(response.getStatus(), equalTo(200));
        assertThat(Tracee.getBackend().get("beforeRequest"), equalTo("no"));
        assertThat(Tracee.getBackend().get("beforeResponse"), equalTo("yes"));

    }

}
