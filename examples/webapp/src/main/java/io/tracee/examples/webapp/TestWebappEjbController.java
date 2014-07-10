package io.tracee.examples.webapp;

import io.tracee.examples.ejb.TestEjb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;


@ManagedBean
@RequestScoped
public class TestWebappEjbController {

    static final Logger LOGGER = LoggerFactory.getLogger(TestWebappEjbController.class);

    @EJB
    private TestEjb testEjb;

    @ManagedProperty(value = "#{payload}")
    private TestWebappPayload payload;

    public void setPayload(TestWebappPayload payload) {
        this.payload = payload;
    }

    public String multiplyEjbRemotely() {
        LOGGER.info("call multiply on remote javaee method");
        if (payload.getFirstArgument() != null && payload.getSecondArgument() != null) {
            payload.setResult(testEjb.multiply(payload.getFirstArgument(), payload.getSecondArgument()));
        }
        return null;
    }

    public String sumEjbRemotely() {
        LOGGER.info("call sum on remote javaee method");
        if (payload.getFirstArgument() != null && payload.getSecondArgument() != null) {
            payload.setResult(testEjb.sum(payload.getFirstArgument(), payload.getSecondArgument()));
        }
        return null;
    }

    public String triggerEjbRemoteError() {
        LOGGER.info("Trigger remote javaee error");
        testEjb.error(payload.getFirstArgument(), payload.getSecondArgument());
        return null;
    }


}
