package de.holisticon.util.tracee.jaxws.server;


import de.holisticon.util.tracee.TraceeConstants;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.embeddable.EJBContainer;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeJaxWsTestServiceIT {

    private static URL ENDPOINT_URL;
    private static final QName ENDPOINT_QNAME = new QName(TraceeJaxWsEndpoint.Descriptor.TNS, "TraceeJaxWsEndpoint");

    @BeforeClass
    public static void setUp() throws Exception {
        ENDPOINT_URL = new URL("http://127.0.0.1:4204/jaxws/TraceeJaxWsEndpointImpl?wsdl");
        Properties p = new Properties();
        p.setProperty("openejb.embedded.remotable", "true");

        p.setProperty("log4j.category.de.holisticon.util.tracee", "info");
        p.setProperty("log4j.appender.C.layout", "org.apache.log4j.PatternLayout");
        p.setProperty("log4j.appender.C.layout.ConversionPattern", "%d{yyyy-MM-dd HH:mm:ss.SSSS} [x-tracee-request:%X{x-tracee-request}] \n %m%n");
        ejbContainer = EJBContainer.createEJBContainer(p);
    }

    private static EJBContainer ejbContainer;

    @AfterClass
    public static void tearDown() {
        ejbContainer.close();
    }

    @Test
    public void testJaxRsServiceCreatesRequestId() throws Exception {
        Service calculatorService = Service.create(ENDPOINT_URL, ENDPOINT_QNAME);

        final TraceeJaxWsEndpoint remote = calculatorService.getPort(TraceeJaxWsEndpoint.class);
        final Map<String, String> result = remote.getCurrentTraceeContext();

        assertThat(result.entrySet(), not(empty()));
        assertThat(result, Matchers.hasKey(TraceeConstants.REQUEST_ID_KEY));

    }

}
