package de.holisticon.util.tracee.examples.webapp;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeLogger;
import de.holisticon.util.tracee.examples.jaxws.client.testclient.TraceeJaxWsTestService;
import de.holisticon.util.tracee.examples.jaxws.client.testclient.TraceeJaxWsTestWS;
import de.holisticon.util.tracee.jaxws.client.TraceeClientHandlerResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Tobias Gindler, holisticon AG on 10.01.14.
 */
@ManagedBean
@RequestScoped
public class TestWebappController {

    final Logger LOGGER = LoggerFactory.getLogger(TestWebappController.class);

    @ManagedProperty(value="#{payload}")
    private TestWebappPayload payload;

    public void setPayload(TestWebappPayload payload) {
        this.payload = payload;
    }

    public String multiply() {
        LOGGER.info("multiply");
        if (payload.getFirstArgument() != null && payload.getSecondArgument() != null) {
            payload.setResult(payload.getFirstArgument() * payload.getSecondArgument());
        }
        return null;
    }

    public String sum() {
        LOGGER.info("summarize");
        if (payload.getFirstArgument() != null && payload.getSecondArgument() != null) {
            payload.setResult(payload.getFirstArgument() + payload.getSecondArgument());
        }
        return null;
    }

    public String multiplyRemotely() {
        LOGGER.info("call multiply remote service method");
        if (payload.getFirstArgument() != null && payload.getSecondArgument() != null) {
            payload.setResult(getServiceClient().multiply(payload.getFirstArgument(), payload.getSecondArgument()));
        }
        return null;
    }

    public String sumRemotely() {
        LOGGER.info("call sum remote service method");
        if (payload.getFirstArgument() != null && payload.getSecondArgument() != null) {
            payload.setResult(getServiceClient().sum(payload.getFirstArgument(), payload.getSecondArgument()));
        }
        return null;
    }

    public String triggerRemoteError() {
        LOGGER.info("Trigger remote error");
        getServiceClient().error(this.payload.getFirstArgument(),this.payload.getSecondArgument());
        return null;

    }

    private TraceeJaxWsTestWS getServiceClient () {

        TraceeJaxWsTestService testWebservice = null;
        try {
            testWebservice = new TraceeJaxWsTestService(
                    new URL("http://localhost:8080/traceeJaxwsTestService/webservices/TraceeJaxWsTestService?wsdl"));
            testWebservice.setHandlerResolver(new TraceeClientHandlerResolver());
        } catch (MalformedURLException e) {
           // should never occur
        }

        return testWebservice.getPort(TraceeJaxWsTestWS.class);
    }


}
