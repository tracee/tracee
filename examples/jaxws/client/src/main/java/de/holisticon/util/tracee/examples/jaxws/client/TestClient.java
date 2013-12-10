package de.holisticon.util.tracee.examples.jaxws.client;


import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.examples.jaxws.client.testclient.TraceeJaxWsTestService;
import de.holisticon.util.tracee.examples.jaxws.client.testclient.TraceeJaxWsTestWS;
import de.holisticon.util.tracee.jaxws.client.TraceeClientHandlerResolver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Tobias Gindler, holisticon AG on 10.12.13.
 */
public class TestClient {

    public static void main(final String[] args) throws MalformedURLException {

        final TraceeBackend traceeBackend = Tracee.getBackend();
        traceeBackend.put(TraceeConstants.REQUEST_ID_KEY, "XXX");


        final TraceeJaxWsTestService testWebservice = new TraceeJaxWsTestService(
                new URL("http://localhost:8081/traceeJaxwsTestService/webservices/TraceeJaxWsTestService?wsdl"));
        testWebservice.setHandlerResolver(new TraceeClientHandlerResolver());


        final TraceeJaxWsTestWS ws = testWebservice.getPort(TraceeJaxWsTestWS.class);

        System.out.println("\n3+2=" + ws.sum(2, 3)
                + "\nREQUEST_ID:" + traceeBackend.get(TraceeConstants.REQUEST_ID_KEY));


        traceeBackend.remove(TraceeConstants.REQUEST_ID_KEY);

    }

}
